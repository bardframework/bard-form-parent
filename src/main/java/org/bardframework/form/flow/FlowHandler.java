package org.bardframework.form.flow;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bardframework.commons.web.utils.WebUtils;
import org.bardframework.form.FormService;
import org.bardframework.form.flow.exception.InvalidateFlowException;
import org.bardframework.form.flow.repository.FlowDataRepository;
import org.bardframework.form.model.Form;
import org.bardframework.form.model.FormField;
import org.bardframework.form.processor.FormProcessor;
import org.bardframework.form.template.FormFieldTemplate;
import org.bardframework.form.template.FormTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class FlowHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlowHandler.class);
    protected final List<FormTemplate> forms;
    protected final FlowDataRepository<FlowData> flowDataRepository;
    protected final MessageSource messageSource;
    protected final FormService formService;

    public FlowHandler(List<FormTemplate> forms, FlowDataRepository<FlowData> flowDataRepository, @Autowired FormService formService, @Autowired MessageSource messageSource) {
        this.forms = forms;
        this.flowDataRepository = flowDataRepository;
        this.formService = formService;
        this.messageSource = messageSource;
    }

    public FlowResponse<String> start(Locale locale, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
        String flowToken = UUID.randomUUID().toString();
        FlowData flowData = new FlowData();
        flowData.setLocale(locale);
        flowData.setCurrentStepIndex(-1);
        FormTemplate formTemplate = forms.get(0);
        FlowResponse<String> response = this.prepareForSend(formTemplate, flowToken, flowData, Map.of(), flowToken, httpRequest, httpResponse);
        flowDataRepository.put(flowToken, flowData);
        return response;
    }

    public int getStepsCounts() {
        return this.forms.size();
    }

    public FlowResponse<String> submit(String flowToken, Map<String, String> formData, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
            throws Exception {
        FlowData flowData = flowDataRepository.get(flowToken);
        FormTemplate currentFormTemplate = forms.get(flowData.getCurrentStepIndex());
        Form currentForm = formService.translate(new Form(), currentFormTemplate, flowData.getLocale(), Map.of());
        formService.validate(currentForm, formData);
        for (FormFieldTemplate fieldTemplate : currentFormTemplate.getFields()) {
            if (!fieldTemplate.isVolatile() && formData.containsKey(fieldTemplate.getName())) {
                String value = formData.get(fieldTemplate.getName());
                flowData.getData().put(fieldTemplate.getName(), WebUtils.escapeString(value));
            }
        }
        FlowResponse<String> response = null;
        try {
            for (FormFieldTemplate fieldTemplate : currentFormTemplate.getFields()) {
                this.process(fieldTemplate.getPostProcessors(), flowToken, flowData, formData, httpRequest, httpResponse);
            }
            this.process(currentFormTemplate.getPostProcessors(), flowToken, flowData, formData, httpRequest, httpResponse);

            FormTemplate nextFormTemplate = forms.get(flowData.getCurrentStepIndex() + 1);
            response = this.prepareForSend(nextFormTemplate, flowToken, flowData, formData, null, httpRequest, httpResponse);
            return response;
        } catch (InvalidateFlowException e) {
            /*
                در برخی موارد مانند تلاش زیاد برای وارد کردن کد otp یا ... خطای InvalidateFlowException تولید می شود و باید داده های مربوط به توکن مربوطه پاک شوند
             */
            this.cleanFlowData(flowToken);
            throw e;
        } finally {
            /*
                در تمامی حالات (حالاتی که استثنا رخ دهد یا خیر) باید دیتای تغیر یافته را ذخیره کنیم؛
                برخی کنترل های امنیتی مانند غیر معتبر کردن کپچای استفاده شده، با پاک کردن از دیتا انجام می شود
                در مثال ذکر شده و در حالتی که یک مرحله پس از کنترل کپچا استثنایی رخ دهد باید دیتا را ذخیره کنیم تا ناسازگاری در فرایند ایجاد نشود.
                توضیحات فوق، بیانگر اهمیت و دلیل ذخیره ی دیتا در بلاک finally است
             */
            if (null == response || !response.isFinished()) {
                flowDataRepository.put(flowToken, flowData);
            }
            if (null != response && response.isFinished()) {
                this.cleanFlowData(flowToken);
            }
        }
    }

    private FlowResponse<String> prepareForSend(FormTemplate formTemplate, String flowToken, FlowData flowData, Map<String, String> formData, String responseData, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
        for (FormFieldTemplate fieldTemplate : formTemplate.getFields()) {
            this.process(fieldTemplate.getPreProcessors(), flowToken, flowData, formData, httpRequest, httpResponse);
        }
        this.process(formTemplate.getPreProcessors(), flowToken, flowData, formData, httpRequest, httpResponse);
        Form nextForm = formService.translate(new Form(), formTemplate, flowData.getLocale(), flowData.getData());
        /*
            fill form
         */
        for (FormField field : nextForm.getFields()) {
            if (flowData.getData().containsKey(field.getName())) {
                field.setValue(flowData.getData().get(field.getName()));
            }
        }
        FlowResponse<String> response = new FlowResponse<>(responseData);
        if (formTemplate.isFinished()) {
            response.setFinished(true);
        } else {
            flowData.setCurrentStepIndex(flowData.getCurrentStepIndex() + 1);
        }
        response.setForm(nextForm);
        response.setSteps(this.getStepsCounts());
        /*
            current start from 1
         */
        response.setCurrent(flowData.getCurrentStepIndex() + 1);
        return response;
    }

    public void process(String flowToken, String action, Map<String, String> formData, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
            throws Exception {
        LOGGER.debug("start action[{}] processing, flow token [{}], form data [{}]", action, flowToken, formData);
        FlowData flowData = flowDataRepository.get(flowToken);
        FormTemplate currentFormTemplate = forms.get(flowData.getCurrentStepIndex());
        if (StringUtils.isBlank(action)) {
            LOGGER.warn("null action can't process, flow token [{}], form [{}]", flowToken, currentFormTemplate.getName());
            httpResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        try {
            boolean process = false;
            for (FormFieldTemplate fieldTemplate : currentFormTemplate.getFields()) {
                process |= this.process(fieldTemplate.getActionProcessors().get(action), flowToken, flowData, formData, httpRequest, httpResponse);
            }
            process |= this.process(currentFormTemplate.getActionProcessors().get(action), flowToken, flowData, formData, httpRequest, httpResponse);
            if (!process) {
                /*
                    در صورتی که عملیات نامناسب درخواست شود، فلو را پاک می کنیم
                 */
                LOGGER.warn("no processor exist to handle action [{}], flow token [{}], form [{}]", action, flowToken, currentFormTemplate.getName());
                throw new InvalidateFlowException(flowToken, "invalid action");
            }
        } catch (InvalidateFlowException e) {
            /*
                در برخی موارد مانند تلاش زیاد برای تولید کپچا و ... خطای InvalidateFlowException تولید می شود و باید داده های مربوط به فلو پاک شوند
             */
            this.cleanFlowData(flowToken);
            throw e;
        } finally {
            flowDataRepository.put(flowToken, flowData);
        }
    }

    private void cleanFlowData(String flowToken) {
        flowDataRepository.remove(flowToken);
    }

    private boolean process(List<FormProcessor> processors, String flowToken, FlowData flowData, Map<String, String> formData, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
        if (CollectionUtils.isEmpty(processors)) {
            return false;
        }
        for (FormProcessor processor : processors) {
            processor.process(flowToken, flowData.getData(), formData, flowData.getLocale(), httpRequest, httpResponse);
        }
        return true;
    }
}
