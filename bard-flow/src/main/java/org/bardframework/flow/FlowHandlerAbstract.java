package org.bardframework.flow;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bardframework.commons.utils.ReflectionUtils;
import org.bardframework.commons.web.utils.WebUtils;
import org.bardframework.flow.exception.InvalidateFlowException;
import org.bardframework.flow.form.FlowFormTemplate;
import org.bardframework.flow.form.FormProcessor;
import org.bardframework.flow.form.field.input.FlowInputFieldTemplate;
import org.bardframework.flow.repository.FlowDataRepository;
import org.bardframework.form.BardForm;
import org.bardframework.form.FormUtils;
import org.bardframework.form.field.input.InputFieldTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

public abstract class FlowHandlerAbstract<D extends FlowData> implements FlowHandler {

    protected static final Logger LOGGER = LoggerFactory.getLogger(FlowHandlerAbstract.class);
    protected final FlowDataRepository<D> flowDataRepository;
    protected final List<FlowFormTemplate> forms;
    protected List<FormProcessor> preProcessors = new ArrayList<>();
    protected List<FormProcessor> postProcessors = new ArrayList<>();
    protected Map<String, List<FormProcessor>> actionProcessors = new HashMap<>();

    public FlowHandlerAbstract(FlowDataRepository<D> flowDataRepository, List<FlowFormTemplate> forms) {
        this.flowDataRepository = flowDataRepository;
        this.forms = forms;
    }

    @Override
    public FlowResponse start(Map<String, String> initValues, Locale locale, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
            throws Exception {
        D flowData = ReflectionUtils.newInstance(ReflectionUtils.getGenericArgType(this.getClass(), 0));
        String flowToken = this.generateFlowToken();
        flowData.setLocale(locale);
        flowData.getData().putAll(initValues);
        flowData.setCurrentFormIndex(-1);
        this.getFlowDataRepository().put(flowToken, flowData);

        try {
            /*
                اجرای پیش پردازش های فلو
             */
            this.process(this.getPreProcessors(flowData), flowToken, flowData, Map.of(), httpRequest, httpResponse);
            return this.processNextForm(flowToken, flowData, Map.of(), httpRequest, httpResponse);
        } finally {
            /*
                در تمامی حالات (حالاتی که استثنا رخ دهد یا خیر) باید دیتای تغیر یافته را ذخیره کنیم؛
                برخی کنترل های امنیتی مانند غیر معتبر کردن کپچای استفاده شده، با پاک کردن از دیتا انجام می شود
                در مثال ذکر شده و در حالتی که یک مرحله پس از کنترل کپچا استثنایی رخ دهد باید دیتا را ذخیره کنیم تا ناسازگاری در فرایند ایجاد نشود.
                توضیحات فوق، بیانگر اهمیت و دلیل ذخیره ی دیتا در بلاک finally است
             */
            this.updateFlowData(flowToken, flowData);
        }
    }

    @Override
    public FlowResponse submit(String flowToken, Map<String, String> formData, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
            throws Exception {
        D flowData = this.getFlowDataRepository().get(flowToken);
        FlowFormTemplate currentFormTemplate = this.getCurrentFormTemplate(flowData);
        currentFormTemplate.validate(flowToken, flowData.getData(), formData, flowData.getLocale(), httpRequest);
        this.fillFlowData(flowData.getData(), formData, currentFormTemplate);
        try {
            this.process(currentFormTemplate.getPostProcessors(), flowToken, flowData, formData, httpRequest, httpResponse);
            return this.processNextForm(flowToken, flowData, formData, httpRequest, httpResponse);
        } catch (InvalidateFlowException ex) {
            this.invalidateFlow(ex);
            throw ex;
        } finally {
            this.updateFlowData(flowToken, flowData);
        }
    }

    @Override
    public void action(String flowToken, String action, Map<String, String> formData, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
            throws Exception {
        LOGGER.debug("start action[{}] processing, flow token [{}], form data [{}]", action, flowToken, formData);
        D flowData = this.getFlowDataRepository().get(flowToken);
        FlowFormTemplate currentFormTemplate = this.getCurrentFormTemplate(flowData);
        if (StringUtils.isBlank(action)) {
            LOGGER.warn("null action can't process, flow token [{}], form [{}]", flowToken, currentFormTemplate.getName());
            httpResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        try {
            if (!this.processAction(flowToken, action, flowData, formData, currentFormTemplate, httpRequest, httpResponse)) {
                /*
                    در صورتی که عملیات نامناسب درخواست شود (هیچ پردازشگری یافت نشود)، فلو را پاک می کنیم
                 */
                LOGGER.warn("no processor exist to handle action [{}], flow token [{}], form [{}]", action, flowToken, currentFormTemplate.getName());
                throw new InvalidateFlowException(flowToken, "invalid action");
            }
        } catch (InvalidateFlowException ex) {
            this.invalidateFlow(ex);
            throw ex;
        } finally {
            this.updateFlowData(flowToken, flowData);
        }
    }

    /**
     * این متد فقط فرم جاری را برمیگرداند و پیش پردازش های آن را اجرا نمی کند
     */
    @Override
    public FlowResponse getCurrent(String flowToken, Locale locale, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
            throws Exception {
        D flowData = this.getFlowDataRepository().get(flowToken);
        if (flowData.getCurrentFormIndex() == 0) {
            flowData.setLocale(locale);
            this.updateFlowData(flowToken, flowData);
        }
        FlowFormTemplate currentFormTemplate = this.getCurrentFormTemplate(flowData);
        return this.toResponse(flowToken, currentFormTemplate, flowData, httpRequest, httpResponse);
    }

    protected boolean processAction(String flowToken, String action, D flowData, Map<String, String> formData, FlowFormTemplate currentFormTemplate, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
        for (FlowInputFieldTemplate<?, ?> fieldTemplate : currentFormTemplate.getFieldTemplates(flowData.getData(), FlowInputFieldTemplate.class)) {
            /*
                تلاش برای پردازش با پردازشگرهای اینپوت ها
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
    protected void fillFlowData(Map<String, String> flowData, Map<String, String> formData, FlowFormTemplate currentFormTemplate) {
        for (InputFieldTemplate<?, ?> inputFieldTemplate : currentFormTemplate.getFieldTemplates(flowData, InputFieldTemplate.class)) {
            if (!inputFieldTemplate.isPersistentValue()) {
                continue;
            }
            String value = formData.remove(inputFieldTemplate.getName());
            if (StringUtils.isNotBlank(value)) {
                flowData.put(inputFieldTemplate.getName(), WebUtils.escapeString(value));
            }
        }
    }

    /**
     * 1. محاسبه فرم بعدی
     * <br>
     * 2. اجرای پیش پردازش های فرم
     * <br>
     * 3. ذخیره داده های فلو
     */
    protected FlowResponse processNextForm(String flowToken, D flowData, Map<String, String> formData, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
        FlowFormTemplate nextFormTemplate = forms.subList(flowData.getCurrentFormIndex() + 1, forms.size()).stream()
                .filter(formTemplate -> formTemplate.mustShow(flowData.getData()))
                .findFirst().orElse(null);
        if (null != nextFormTemplate) {
            this.process(nextFormTemplate.getPreProcessors(), flowToken, flowData, formData, httpRequest, httpResponse);
            /*
                فراخوانی پیش پردازش های فیلد ها (مانند ارسال پیامک یا ...)
             */
            for (FlowInputFieldTemplate<?, ?> flowInputFieldTemplate : nextFormTemplate.getFieldTemplates(formData, FlowInputFieldTemplate.class)) {
                flowInputFieldTemplate.preProcess(flowToken, flowData.getData(), flowData.getLocale(), httpResponse);
            }
            /*
                تنظیم کردن اندیس فرم جاری باید پس از موفقیت آمیز بودن اجرای preProcessهای آن باشد
             */
            flowData.setCurrentFormIndex(this.forms.indexOf(nextFormTemplate));
        }
        FlowResponse response = this.toResponse(flowToken, nextFormTemplate, flowData, httpRequest, httpResponse);
        if (Boolean.TRUE.equals(response.getFinished())) {
            this.onFinished(flowToken, flowData, formData, httpRequest, httpResponse);
        }
        return response;
    }

    protected FlowFormTemplate getCurrentFormTemplate(D flowData) throws Exception {
        return this.forms.get(flowData.getCurrentFormIndex());
    }

    protected void onFinished(String flowToken, D flowData, Map<String, String> formData, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
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
            در برخی موارد مانند تلاش زیاد برای تولید کپچا و ... خطای InvalidateFlowException تولید می شود و باید داده های مربوط به فلو پاک شوند
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
            BardForm nextForm = FormUtils.toForm(formTemplate, flowData.getData(), flowData.getData(), flowData.getLocale(), httpRequest);
            response.setForm(nextForm).setSteps(this.getStepsCounts(flowData)).setCurrent(flowData.getCurrentFormIndex());
        }
        if (null == formTemplate || formTemplate.isFinished()) {
            response.setFinished(Boolean.TRUE);
        } else {
            /*
                در حالتی که فلو به پایان می رسد؛ نیازی به ست کردن فلو توکن نیست
             */
            httpResponse.setHeader(TOKEN_HEADER_NAME, flowToken);
        }
        return response;
    }

    protected int getStepsCounts(D flowData) {
        return this.forms.size();
    }

    protected void cleanFlowData(String flowToken) {
        this.getFlowDataRepository().remove(flowToken);
    }

    protected boolean process(List<FormProcessor> processors, String flowToken, D flowData, Map<String, String> formData, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
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

    public FlowDataRepository<D> getFlowDataRepository() {
        return flowDataRepository;
    }

    public List<FormProcessor> getPreProcessors(D flowData) {
        return preProcessors;
    }

    public void setPreProcessors(List<FormProcessor> preProcessors) {
        this.preProcessors = preProcessors;
    }

    public List<FormProcessor> getPostProcessors(D flowData) {
        return postProcessors;
    }

    public void setPostProcessors(List<FormProcessor> postProcessors) {
        this.postProcessors = postProcessors;
    }

    public Map<String, List<FormProcessor>> getActionProcessors(D flowData) {
        return actionProcessors;
    }

    public void setActionProcessors(Map<String, List<FormProcessor>> actionProcessors) {
        this.actionProcessors = actionProcessors;
    }
}
