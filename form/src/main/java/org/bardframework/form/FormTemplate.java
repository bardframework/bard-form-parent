package org.bardframework.form;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.bardframework.form.exception.FormDataValidationException;
import org.bardframework.form.field.FieldTemplate;
import org.bardframework.form.field.input.InputField;
import org.bardframework.form.field.input.InputFieldTemplate;
import org.bardframework.form.flow.FlowData;
import org.bardframework.form.processor.FormProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.SpelCompilerMode;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

public class FormTemplate extends Form {

    protected final MessageSource messageSource;
    private final List<FieldTemplate<?>> fieldTemplates;
    private Class<?> dtoClass;
    private List<FormProcessor> preProcessors;
    private List<FormProcessor> postProcessors;
    private Map<String, List<FormProcessor>> actionProcessors = new HashMap<>();
    private boolean finished;
    protected Expression showExpression = null;

    public FormTemplate(String name, List<FieldTemplate<?>> fieldTemplates, @Autowired MessageSource messageSource) {
        this.name = name;
        this.fieldTemplates = fieldTemplates;
        this.messageSource = messageSource;
    }

    @PostConstruct
    protected void configurationValidate() {
        if (CollectionUtils.isNotEmpty(postProcessors)) {
            if (finished) {
                throw new IllegalStateException("when finished is true, can't set postProcessors");
            }
            this.postProcessors.forEach(processor -> processor.configurationValidate(this));
        }
        if (CollectionUtils.isNotEmpty(preProcessors)) {
            this.preProcessors.forEach(processor -> processor.configurationValidate(this));
        }
        List<InputFieldTemplate<?, ?>> inputFieldTemplates = new ArrayList<>();
        fieldTemplates.stream().filter(fieldTemplate -> fieldTemplate instanceof InputFieldTemplate).forEach(fieldTemplate -> {
            inputFieldTemplates.add((InputFieldTemplate<?, ?>) fieldTemplate);
        });
        /*
            merge all pre processors
         */
        List<FormProcessor> processors = new ArrayList<>();
        for (InputFieldTemplate<?, ?> inputFieldTemplate : inputFieldTemplates) {
            if (null != inputFieldTemplate.getPreProcessors()) {
                processors.addAll(inputFieldTemplate.getPreProcessors());
            }
        }
        if (null != this.preProcessors) {
            processors.addAll(this.preProcessors);
        }
        processors.sort(FormProcessor::compareTo);
        this.preProcessors = processors;

        /*
            merge all post processors
         */
        processors = new ArrayList<>();
        for (InputFieldTemplate<?, ?> inputFieldTemplate : inputFieldTemplates) {
            if (null != inputFieldTemplate.getPostProcessors()) {
                processors.addAll(inputFieldTemplate.getPostProcessors());
            }
        }
        if (null != this.postProcessors) {
            processors.addAll(this.postProcessors);
        }
        processors.sort(FormProcessor::compareTo);
        this.postProcessors = processors;
    }

    public <F extends InputField<T>, T> void validate(FlowData flowData, Map<String, String> values) throws Exception {
        FormDataValidationException ex = new FormDataValidationException();
        for (InputFieldTemplate<?, ?> inputFieldTemplate : this.getInputFieldTemplates(flowData)) {
            InputFieldTemplate<F, T> template = (InputFieldTemplate<F, T>) inputFieldTemplate;
            F formField = template.toField(this, flowData.getFlowData(), flowData.getLocale());
            if (Boolean.TRUE.equals(formField.getDisable())) {
                continue;
            }
            String stringValue = values.get(template.getName());
            if (!template.isValid(formField, template.toValue(stringValue))) {
                ex.addFiledError(template.getName(), formField.getErrorMessage());
            }
        }
        if (!MapUtils.isEmpty(ex.getInvalidFields())) {
            throw ex;
        }
    }

    public List<InputFieldTemplate<?, ?>> getInputFieldTemplates(FlowData flowData) {
        List<InputFieldTemplate<?, ?>> inputFieldTemplates = new ArrayList<>();
        for (FieldTemplate<?> fieldTemplate : this.getFieldTemplates(flowData)) {
            if (!(fieldTemplate instanceof InputFieldTemplate<?, ?>)) {
                continue;
            }
            inputFieldTemplates.add((InputFieldTemplate<?, ?>) fieldTemplate);
        }
        return inputFieldTemplates;
    }

    /**
     * محاسبه لیست فیلدهایی که استفاده کننده مجاز به ارسال دیتا برای آن هاست
     * این فیلدها شامل تمامی اینپوت فیلدهایی است که فعال باشند
     */
    public Set<String> getAllowedInputFields(FlowData flowData) throws Exception {
        List<InputFieldTemplate<?, ?>> inputFieldTemplates = this.getInputFieldTemplates(flowData);
        Set<String> allowedFieldNames = new HashSet<>();
        for (InputFieldTemplate<?, ?> inputFieldTemplate : inputFieldTemplates) {
            Boolean disable = FormUtils.getFieldBooleanProperty(this, inputFieldTemplate, "disable", flowData.getLocale(), flowData.getFlowData(), null);
            if (!Boolean.TRUE.equals(disable)) {
                allowedFieldNames.add(inputFieldTemplate.getName());
            }
        }
        return allowedFieldNames;
    }

    public void setShowExpression(String showExpression) {
        this.showExpression = new SpelExpressionParser(new SpelParserConfiguration(SpelCompilerMode.IMMEDIATE, null)).parseExpression(showExpression);
    }

    public boolean mustShow(FlowData flowData) {
        return null == showExpression || Boolean.TRUE.equals(showExpression.getValue(new StandardEvaluationContext(flowData), Boolean.class));
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

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public List<FieldTemplate<?>> getFieldTemplates(FlowData flowData) {
        return fieldTemplates.stream().filter(fieldTemplate -> fieldTemplate.mustShow(flowData)).collect(Collectors.toList());
    }

    public Map<String, List<FormProcessor>> getActionProcessors() {
        return actionProcessors;
    }

    public void setActionProcessors(Map<String, List<FormProcessor>> actionProcessors) {
        this.actionProcessors = actionProcessors;
    }

    public FieldTemplate<?> getField(String name, FlowData flowData) {
        return this.getFieldTemplates(flowData).stream().filter(field -> field.getName().equals(name)).findFirst().orElse(null);
    }

    public Class<?> getDtoClass() {
        return dtoClass;
    }

    public void setDtoClass(Class<?> dtoClass) {
        this.dtoClass = dtoClass;
    }

    public MessageSource getMessageSource() {
        return messageSource;
    }
}