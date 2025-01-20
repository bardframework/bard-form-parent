package org.bardframework.flow;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bardframework.commons.utils.ReflectionUtils;
import org.bardframework.flow.exception.FlowDataValidationException;
import org.bardframework.flow.exception.InvalidateFlowException;
import org.bardframework.flow.form.FlowFormTemplate;
import org.bardframework.flow.form.FormProcessor;
import org.bardframework.flow.form.field.input.FlowInputFieldTemplate;
import org.bardframework.flow.repository.FlowDataRepository;
import org.bardframework.form.BardForm;
import org.bardframework.form.FormUtils;
import org.bardframework.form.exception.FormDataValidationException;
import org.bardframework.form.field.input.InputFieldTemplateAbstract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Getter
@Setter
public abstract class FlowHandlerAbstract<D extends FlowData> implements FlowHandler {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    protected final String name;
    protected final FlowDataRepository<D> flowDataRepository;
    protected final Supplier<List<FlowFormTemplate>> formsSupplier;
    protected List<FormProcessor> preProcessors = new ArrayList<>();
    protected List<FormProcessor> postProcessors = new ArrayList<>();
    protected Map<String, List<FormProcessor>> actionProcessors = new HashMap<>();

    @Autowired
    public FlowHandlerAbstract(String name, FlowDataRepository<D> flowDataRepository, List<FlowFormTemplate> forms) {
        this(name, flowDataRepository, () -> forms);
    }

    public FlowHandlerAbstract(String name, FlowDataRepository<D> flowDataRepository, Supplier<List<FlowFormTemplate>> formsSupplier) {
        this.name = name;
        this.flowDataRepository = flowDataRepository;
        this.formsSupplier = formsSupplier;
    }

    @Override
    public FlowResponse start(Map<String, Object> initValues, Locale locale, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
            throws Exception {
        D flowData = ReflectionUtils.newInstance(ReflectionUtils.getGenericArgType(this.getClass(), 0));
        String flowToken = this.generateFlowToken(initValues);
        flowData.setLocale(locale);
        flowData.getData().putAll(initValues);
        flowData.setCurrentFormIndex(-1);
        this.getFlowDataRepository().put(flowToken, flowData);
        FlowFormTemplate currentFormTemplate = null;
        try {
            /*
                اجرای پیش پردازش‌های فلو
             */
            this.process(this.getPreProcessors(flowData), flowToken, flowData, Map.of(), httpRequest, httpResponse);
            currentFormTemplate = this.getCurrentFormTemplate(flowData);
            return this.processNextForm(flowToken, flowData, Map.of(), httpRequest, httpResponse);
        } catch (Exception e) {
            return this.handleExceptionInternal(flowToken, flowData, initValues, currentFormTemplate, FlowAction.START, httpRequest, httpResponse, e);
        } finally {
            /*
                در تمامی حالات (حالاتی که استثنا رخ دهد یا خیر) باید دیتای تغیر یافته را ذخیره کنیم؛
                برخی کنترل‌های امنیتی مانند غیر معتبر کردن کپچای استفاده شده، با پاک کردن از دیتا انجام می شود
                در مثال ذکر شده و در حالتی که یک مرحله پس از کنترل کپچا استثنایی رخ دهد باید دیتا را ذخیره کنیم تا ناسازگاری در فرایند ایجاد نشود.
                توضیحات فوق، بیانگر اهمیت و دلیل ذخیره ی دیتا در بلاک finally است
             */
            this.updateFlowData(flowToken, flowData);
        }
    }

    @Override
    public FlowResponse submit(String flowToken, Map<String, Object> formData, Locale locale, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
            throws Exception {
        D flowData = null;
        FlowFormTemplate currentFormTemplate = null;
        try {
            flowData = this.getFlowDataRepository().get(flowToken);
            currentFormTemplate = this.getCurrentFormTemplate(flowData);
        } catch (Exception ex) {
            return this.handleExceptionInternal(flowToken, flowData, formData, currentFormTemplate, FlowAction.SUBMIT_FORM, httpRequest, httpResponse, ex);
        }
        try {
            if (!Objects.equals(locale, flowData.getLocale())) {
                this.onLocaleChange(flowToken, flowData, formData, FlowAction.SUBMIT_FORM, flowData.getLocale(), locale, httpRequest, httpResponse);
            }
            FormUtils.validate(flowToken, currentFormTemplate, flowData.getData(), formData, flowData.getLocale(), httpRequest, httpResponse);
            this.fillFlowData(flowData.getData(), formData, currentFormTemplate, httpRequest, httpResponse);
            this.process(currentFormTemplate.getPostProcessors(), flowToken, flowData, formData, httpRequest, httpResponse);
            return this.processNextForm(flowToken, flowData, formData, httpRequest, httpResponse);
        } catch (Exception ex) {
            return this.handleExceptionInternal(flowToken, flowData, formData, currentFormTemplate, FlowAction.SUBMIT_FORM, httpRequest, httpResponse, ex);
        } finally {
            this.updateFlowData(flowToken, flowData);
        }
    }

    @Override
    public Object action(String flowToken, String action, Map<String, Object> formData, Locale locale, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
            throws Exception {
        log.debug("start action[{}] processing, flow token [{}], form data [{}]", action, flowToken, formData);
        D flowData = null;
        FlowFormTemplate currentFormTemplate = null;
        try {
            flowData = this.getFlowDataRepository().get(flowToken);
            currentFormTemplate = this.getCurrentFormTemplate(flowData);
            if (StringUtils.isBlank(action)) {
                log.warn("null action can't process, flow token [{}], form [{}]", flowToken, currentFormTemplate.getName());
                httpResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return null;
            }
            if (!Objects.equals(locale, flowData.getLocale())) {
                this.onLocaleChange(flowToken, flowData, formData, FlowAction.PROCESS_ACTION, flowData.getLocale(), locale, httpRequest, httpResponse);
            }
            if (!this.processAction(flowToken, action, flowData, formData, currentFormTemplate, httpRequest, httpResponse)) {
                /*
                    در صورتی که عملیات نامناسب درخواست شود (هیچ پردازشگری یافت نشود)، فلو را پاک می کنیم
                 */
                log.warn("no processor exist to handle action [{}], flow token [{}], form [{}]", action, flowToken, currentFormTemplate.getName());
                throw new InvalidateFlowException(flowToken, "invalid action", null);
            }
        } catch (Exception ex) {
            return this.handleExceptionInternal(flowToken, flowData, formData, currentFormTemplate, FlowAction.PROCESS_ACTION, httpRequest, httpResponse, ex);
        } finally {
            this.updateFlowData(flowToken, flowData);
        }
        return null;
    }

    /**
     * این متد فقط فرم جاری را برمیگرداند و پیش پردازش‌های آن را اجرا نمی کند
     */
    @Override
    public FlowResponse getCurrent(String flowToken, Locale locale, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
            throws Exception {
        D flowData = null;
        FlowFormTemplate currentFormTemplate = null;
        try {
            flowData = this.getFlowDataRepository().get(flowToken);
            if (flowData.getCurrentFormIndex() == 0) {
                flowData.setLocale(locale);
                this.updateFlowData(flowToken, flowData);
            }
            currentFormTemplate = this.getCurrentFormTemplate(flowData);
            if (!Objects.equals(locale, flowData.getLocale())) {
                this.onLocaleChange(flowToken, flowData, Map.of(), FlowAction.GET_CURRENT, flowData.getLocale(), locale, httpRequest, httpResponse);
            }
            return this.toResponse(flowToken, currentFormTemplate, flowData, httpRequest, httpResponse);
        } catch (Exception ex) {
            return this.handleExceptionInternal(flowToken, flowData, Map.of(), currentFormTemplate, FlowAction.GET_CURRENT, httpRequest, httpResponse, ex);
        }
    }

    protected boolean processAction(String flowToken, String action, D flowData, Map<String, Object> formData, FlowFormTemplate currentFormTemplate, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
        for (FlowInputFieldTemplate<?, ?> fieldTemplate : currentFormTemplate.getFieldTemplates(flowData.getData(), formData, FlowInputFieldTemplate.class, httpRequest, httpResponse)) {
            /*
                تلاش برای پردازش با پردازشگرهای اینپوت‌ها
             */
            if (this.process(fieldTemplate.getActionProcessors().get(action), flowToken, flowData, formData, httpRequest, httpResponse)) {
                return true;
            }
        }
        /*
            تلاش برای پردازش با پردازشگرهای فرم
         */
        if (this.process(currentFormTemplate.getActionProcessors().get(action), flowToken, flowData, formData, httpRequest, httpResponse)) {
            return true;
        }
        /*
            تلاش برای پردازش با پردازشگرهای فلو
         */
        return this.process(this.getActionProcessors(flowData).get(action), flowToken, flowData, formData, httpRequest, httpResponse);
    }

    /**
     * افزودن دیتاهای مورد نیاز در فرم به فلو دیتا
     */
    protected void fillFlowData(Map<String, Object> flowData, Map<String, Object> formData, FlowFormTemplate currentFormTemplate, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        for (InputFieldTemplateAbstract<?, ?> inputFieldTemplate : currentFormTemplate.getFieldTemplates(flowData, formData, InputFieldTemplateAbstract.class, httpRequest, httpResponse)) {
            if (!inputFieldTemplate.isPersistentValue()) {
                continue;
            }
            Object value = formData.get(inputFieldTemplate.getName());
            if (Boolean.TRUE.equals(currentFormTemplate.getSubmitEmptyInputs()) || null != value) {
                flowData.put(inputFieldTemplate.getName(), value);
            }
        }
    }

    /**
     * 1. محاسبه فرم بعدی
     * <br>
     * 2. اجرای پیش پردازش‌های فرم
     * <br>
     * 3. ذخیره داده‌های فلو
     */
    protected FlowResponse processNextForm(String flowToken, D flowData, Map<String, Object> formData, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
        FlowFormTemplate nextFormTemplate = this.getForms().subList(flowData.getCurrentFormIndex() + 1, this.getForms().size()).stream()
                .filter(formTemplate -> formTemplate.mustShow(flowData.getData()))
                .findFirst().orElse(null);
        if (null != nextFormTemplate) {
            this.process(nextFormTemplate.getPreProcessors(), flowToken, flowData, formData, httpRequest, httpResponse);
            /*
                فراخوانی پیش پردازش‌های فیلد‌ها (مانند ارسال پیامک یا ...)
             */
            List<FlowInputFieldTemplate> flowInputFieldTemplates = nextFormTemplate.getFieldTemplates(flowData.getData(), formData, FlowInputFieldTemplate.class, httpRequest, httpResponse).stream()
                    .filter(Objects::nonNull)
                    .filter(flowInputFieldTemplate -> flowInputFieldTemplate instanceof FlowInputFieldTemplate<?, ?>)
                    .toList();
            for (FlowInputFieldTemplate<?, ?> flowInputFieldTemplate : flowInputFieldTemplates) {
                flowInputFieldTemplate.preProcess(flowToken, flowData.getData(), flowData.getLocale(), httpResponse);
            }
            /*
                تنظیم کردن اندیس فرم جاری باید پس از موفقیت آمیز بودن اجرای preProcessهای آن باشد
             */
            flowData.setCurrentFormIndex(this.getForms().indexOf(nextFormTemplate));
        }
        FlowResponse response = this.toResponse(flowToken, nextFormTemplate, flowData, httpRequest, httpResponse);
        if (Boolean.TRUE.equals(response.getFinished())) {
            this.onFinished(flowToken, flowData, formData, httpRequest, httpResponse);
        }
        return response;
    }

    protected FlowFormTemplate getCurrentFormTemplate(D flowData) throws Exception {
        return this.getForms().get(Math.max(flowData.getCurrentFormIndex(), 0));
    }

    protected void onFinished(String flowToken, D flowData, Map<String, Object> formData, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
        this.process(this.getPostProcessors(flowData), flowToken, flowData, formData, httpRequest, httpResponse);
        this.cleanFlowData(flowToken);
    }

    protected void updateFlowData(String flowToken, D flowData) {
        /*
            در صورتی که توکن معتبر باشد (قبلا بر اثر خطا یا ... داده ی آن پاک نشده باشد) دیتای مربوط به آن را بروز می کنیم
         */
        if (this.getFlowDataRepository().contains(flowToken)) {
            this.getFlowDataRepository().put(flowToken, flowData);
        }
    }

    protected void invalidateFlow(InvalidateFlowException ex) {
        /*
            در برخی موارد مانند تلاش زیاد برای تولید کپچا و ... خطای InvalidateFlowException تولید می شود و باید داده‌های مربوط به فلو پاک شوند
         */
        this.cleanFlowData(ex.getFlowToken());
    }

    /**
     * تبدیل به فرم
     */
    protected FlowResponse toResponse(String flowToken, FlowFormTemplate formTemplate, D flowData, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
        FlowResponse response = new FlowResponse();
        if (null != formTemplate) {
            /*
                convert and fill form
             */
            BardForm nextForm = FormUtils.toForm(formTemplate, flowData.getData(), flowData.getData(), flowData.getLocale(), httpRequest, httpResponse);
            response.setForm(nextForm).setSteps(this.getStepsCounts(flowData)).setCurrent(flowData.getCurrentFormIndex());
        }
        if (null == formTemplate || formTemplate.isFinished()) {
            response.finished();
        } else {
            /*
                در حالتی که فلو به پایان می رسد؛ نیازی به ست کردن فلو توکن نیست
             */
            response.setId(flowToken);
        }
        return response;
    }

    protected FlowResponse handleExceptionInternal(String flowToken, D flowData, Map<String, Object> formData, FlowFormTemplate currentFormTemplate, FlowAction flowAction, HttpServletRequest httpRequest, HttpServletResponse httpResponse, Exception ex) throws Exception {
        if (ex instanceof InvalidateFlowException e) {
            this.invalidateFlow(e);
        } else if (ex instanceof FlowDataValidationException e) {
            if (e.isSendCurrentForm() && null != currentFormTemplate) {
                e.setForm(FormUtils.toForm(currentFormTemplate, null == flowData ? null : flowData.getData(), null == flowData ? null : flowData.getData(), null == flowData ? null : flowData.getLocale(), httpRequest, httpResponse));
            }
        }
        if (!(ex instanceof FlowDataValidationException || ex instanceof FormDataValidationException)) {
            log.error("error in flow [{}].", flowToken, ex);
        }
        return this.handleException(flowToken, flowData, formData, currentFormTemplate, flowAction, httpRequest, httpResponse, ex);
    }

    protected abstract FlowResponse handleException(String flowToken, D flowData, Map<String, Object> formData, FlowFormTemplate currentFormTemplate, FlowAction flowAction, HttpServletRequest httpRequest, HttpServletResponse httpResponse, Exception ex) throws Exception;

    protected int getStepsCounts(D flowData) {
        return this.getForms().size();
    }

    protected void cleanFlowData(String flowToken) {
        this.getFlowDataRepository().remove(flowToken);
    }

    protected boolean process(List<FormProcessor> processors, String flowToken, D flowData, Map<String, Object> formData, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
        if (CollectionUtils.isEmpty(processors)) {
            return false;
        }
        List<FormProcessor> executableProcessors = processors.stream().filter(formProcessor -> formProcessor.mustExecute(flowData.getData())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(executableProcessors)) {
            return false;
        }
        for (FormProcessor processor : executableProcessors) {
            processor.process(flowToken, flowData.getData(), formData, flowData.getLocale(), httpRequest, httpResponse);
        }
        return true;
    }

    protected void onLocaleChange(String flowToken, D flowData, Map<String, Object> formData, FlowAction flowAction, Locale startLocale, Locale newLocale, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
    }

    public List<FormProcessor> getPreProcessors(D flowData) {
        return preProcessors;
    }

    public List<FormProcessor> getPostProcessors(D flowData) {
        return postProcessors;
    }

    public Map<String, List<FormProcessor>> getActionProcessors(D flowData) {
        return actionProcessors;
    }

    public List<FlowFormTemplate> getForms() {
        return this.formsSupplier.get();
    }

    public enum FlowAction {
        START, SUBMIT_FORM, GET_CURRENT, PROCESS_ACTION
    }
}
