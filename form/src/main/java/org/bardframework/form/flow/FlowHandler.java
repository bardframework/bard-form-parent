package org.bardframework.form.flow;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bardframework.commons.web.utils.WebUtils;
import org.bardframework.form.Form;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;
import org.bardframework.form.exception.InvalidateFlowException;
import org.bardframework.form.field.FieldTemplate;
import org.bardframework.form.field.input.InputFieldTemplate;
import org.bardframework.form.flow.repository.FlowDataRepository;
import org.bardframework.form.processor.FormProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class FlowHandler {

    protected static final Logger LOGGER = LoggerFactory.getLogger(FlowHandler.class);
    protected final List<FormTemplate> forms;
    protected final FlowDataRepository<FlowData> flowDataRepository;
    private List<FormProcessor> preProcessors;
    private List<FormProcessor> postProcessors;
    private Map<String, List<FormProcessor>> actionProcessors = new HashMap<>();

    public FlowHandler(List<FormTemplate> forms, FlowDataRepository<FlowData> flowDataRepository) {
        this.forms = forms;
        this.flowDataRepository = flowDataRepository;
    }

    public FlowResponse<String> start(Locale locale, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
        String flowToken = UUID.randomUUID().toString();
        FlowData flowData = new FlowData();
        flowData.setLocale(locale);
        flowData.setCurrentStepIndex(-1);
        FormTemplate formTemplate = this.getNextFormTemplate(flowData);
        this.process(this.getPreProcessors(), flowToken, flowData, Map.of(), httpRequest, httpResponse);
        FlowResponse<String> response = this.prepareForSend(formTemplate, flowToken, flowData, Map.of(), flowToken, httpRequest, httpResponse);
        flowDataRepository.put(flowToken, flowData);
        return response;
    }

    public FlowResponse<String> submit(String flowToken, Map<String, String> formData, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
            throws Exception {
        FlowData flowData = flowDataRepository.get(flowToken);
        FormTemplate currentFormTemplate = forms.get(flowData.getCurrentStepIndex());
        currentFormTemplate.validate(formData, flowData.getData(), flowData.getLocale());
        for (FieldTemplate<?> fieldTemplate : currentFormTemplate.getFieldTemplates()) {
            if (!(fieldTemplate instanceof InputFieldTemplate<?, ?>)) {
                continue;
            }
            InputFieldTemplate<?, ?> inputFieldTemplate = (InputFieldTemplate<?, ?>) fieldTemplate;
            if (!inputFieldTemplate.isPersistentValue()) {
                continue;
            }
            String value = formData.remove(fieldTemplate.getName());
            if (StringUtils.isNotBlank(value)) {
                flowData.getData().put(fieldTemplate.getName(), WebUtils.escapeString(value));
            }
        }
        FlowResponse<String> response = null;
        try {
            this.process(currentFormTemplate.getPostProcessors(), flowToken, flowData, formData, httpRequest, httpResponse);

            FormTemplate nextFormTemplate = this.getNextFormTemplate(flowData);
            response = this.prepareForSend(nextFormTemplate, flowToken, flowData, formData, null, httpRequest, httpResponse);
            if (Boolean.TRUE.equals(response.getFinished())) {
                this.onFinished(flowToken, flowData, formData, httpRequest, httpResponse);
            }
            flowDataRepository.put(flowToken, flowData);
            return response;
        } catch (InvalidateFlowException ex) {
            this.onException(ex);
            throw ex;
        } catch (Exception ex) {
            /*
                در تمامی حالات (حالاتی که استثنا رخ دهد یا خیر) باید دیتای تغیر یافته را ذخیره کنیم؛
                برخی کنترل های امنیتی مانند غیر معتبر کردن کپچای استفاده شده، با پاک کردن از دیتا انجام می شود
                در مثال ذکر شده و در حالتی که یک مرحله پس از کنترل کپچا استثنایی رخ دهد باید دیتا را ذخیره کنیم تا ناسازگاری در فرایند ایجاد نشود.
                توضیحات فوق، بیانگر اهمیت و دلیل ذخیره ی دیتا در بلاک finally است
             */
            flowDataRepository.put(flowToken, flowData);
            throw ex;
        }
    }

    public void action(String flowToken, String action, Map<String, String> formData, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
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
            for (FieldTemplate<?> fieldTemplate : currentFormTemplate.getFieldTemplates()) {
                if (process || !(fieldTemplate instanceof InputFieldTemplate<?, ?>)) {
                    continue;
                }
                InputFieldTemplate<?, ?> inputFieldTemplate = (InputFieldTemplate<?, ?>) fieldTemplate;
                process = this.process(inputFieldTemplate.getActionProcessors().get(action), flowToken, flowData, formData, httpRequest, httpResponse);
            }
            if (!process) {
                process = this.process(currentFormTemplate.getActionProcessors().get(action), flowToken, flowData, formData, httpRequest, httpResponse);
            }
            if (!process) {
                process = this.process(this.getActionProcessors().get(action), flowToken, flowData, formData, httpRequest, httpResponse);
            }
            if (!process) {
                /*
                    در صورتی که عملیات نامناسب درخواست شود، فلو را پاک می کنیم
                 */
                LOGGER.warn("no processor exist to handle action [{}], flow token [{}], form [{}]", action, flowToken, currentFormTemplate.getName());
                throw new InvalidateFlowException(flowToken, "invalid action");
            }
        } catch (InvalidateFlowException ex) {
            this.onException(ex);
            throw ex;
        } finally {
            flowDataRepository.put(flowToken, flowData);
        }
    }

    protected FormTemplate getNextFormTemplate(FlowData flowData) throws Exception {
        return forms.get(flowData.getCurrentStepIndex() + 1);
    }

    protected void onFinished(String flowToken, FlowData flowData, Map<String, String> formData, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
        this.process(this.getPostProcessors(), flowToken, flowData, formData, httpRequest, httpResponse);
        this.cleanFlowData(flowToken);
    }

    protected void onException(InvalidateFlowException ex) {
        /*
            در برخی موارد مانند تلاش زیاد برای تولید کپچا و ... خطای InvalidateFlowException تولید می شود و باید داده های مربوط به فلو پاک شوند
         */
        this.cleanFlowData(ex.getFlowToken());
    }

    protected FlowResponse<String> prepareForSend(FormTemplate formTemplate, String flowToken, FlowData flowData, Map<String, String> formData, String responseData, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
        this.process(formTemplate.getPreProcessors(), flowToken, flowData, formData, httpRequest, httpResponse);
        /*
            convert and fill form
         */
        Form nextForm = FormUtils.toForm(formTemplate, flowData.getLocale(), flowData.getData(), flowData.getData());
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

    protected int getStepsCounts() {
        return this.forms.size();
    }

    protected void cleanFlowData(String flowToken) {
        flowDataRepository.remove(flowToken);
    }

    protected boolean process(List<FormProcessor> processors, String flowToken, FlowData flowData, Map<String, String> formData, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
        if (CollectionUtils.isEmpty(processors)) {
            return false;
        }
        for (FormProcessor processor : processors) {
            processor.process(flowToken, flowData.getData(), formData, flowData.getLocale(), httpRequest, httpResponse);
        }
        return true;
    }

    public List<FormTemplate> getForms() {
        return forms;
    }

    public FlowDataRepository<FlowData> getFlowDataRepository() {
        return flowDataRepository;
    }

    public List<FormProcessor> getPreProcessors() {
        return preProcessors;
    }

    public void setPreProcessors(List<FormProcessor> preProcessors) {
        this.preProcessors = preProcessors;
    }

    public List<FormProcessor> getPostProcessors() {
        return postProcessors;
    }

    public void setPostProcessors(List<FormProcessor> postProcessors) {
        this.postProcessors = postProcessors;
    }

    public Map<String, List<FormProcessor>> getActionProcessors() {
        return actionProcessors;
    }

    public void setActionProcessors(Map<String, List<FormProcessor>> actionProcessors) {
        this.actionProcessors = actionProcessors;
    }
}
