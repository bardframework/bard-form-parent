package org.bardframework.form;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.bardframework.form.common.FormField;
import org.bardframework.form.exception.FormDataValidationException;
import org.bardframework.form.field.base.FieldTemplate;
import org.bardframework.form.field.base.FormFieldTemplate;
import org.bardframework.form.processor.FormProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import javax.annotation.PostConstruct;
import java.util.*;

public class FormTemplate extends Form {

    protected final MessageSource messageSource;
    private final List<FieldTemplate<?>> fieldTemplates;
    private Class<?> dtoClass;
    private List<FormProcessor> preProcessors;
    private List<FormProcessor> postProcessors;
    private Map<String, List<FormProcessor>> actionProcessors = new HashMap<>();
    private boolean finished;

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
        /*
            merge all pre processors
         */
        List<FormProcessor> processors = new ArrayList<>();
        for (FieldTemplate<?> fieldTemplate : this.getFieldTemplates()) {
            if (!(fieldTemplate instanceof FormFieldTemplate<?, ?>)) {
                continue;
            }
            FormFieldTemplate<?, ?> formFieldTemplate = (FormFieldTemplate<?, ?>) fieldTemplate;
            if (null != formFieldTemplate.getPreProcessors()) {
                processors.addAll(formFieldTemplate.getPreProcessors());
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
        for (FieldTemplate<?> fieldTemplate : this.getFieldTemplates()) {
            if (!(fieldTemplate instanceof FormFieldTemplate<?, ?>)) {
                continue;
            }
            FormFieldTemplate<?, ?> formFieldTemplate = (FormFieldTemplate<?, ?>) fieldTemplate;
            if (null != formFieldTemplate.getPostProcessors()) {
                processors.addAll(formFieldTemplate.getPostProcessors());
            }
        }
        if (null != this.postProcessors) {
            processors.addAll(this.postProcessors);
        }
        processors.sort(FormProcessor::compareTo);
        this.postProcessors = processors;
    }

    public <F extends FormField<T>, T> void validate(Map<String, String> values, Map<String, String> args, Locale locale) throws Exception {
        FormDataValidationException ex = new FormDataValidationException();
        for (FieldTemplate<?> fieldTemplate : this.getFieldTemplates()) {
            if (!(fieldTemplate instanceof FormFieldTemplate<?, ?>)) {
                continue;
            }
            FormFieldTemplate<F, T> formFieldTemplate = (FormFieldTemplate<F, T>) fieldTemplate;
            F formField = formFieldTemplate.toField(this, args, locale);
            if (Boolean.TRUE.equals(formField.getDisable())) {
                continue;
            }
            String stringValue = values.get(formFieldTemplate.getName());
            if (!formFieldTemplate.isValid(formField, formFieldTemplate.toValue(stringValue))) {
                ex.addFiledError(formFieldTemplate.getName(), formField.getErrorMessage());
            }
        }
        if (!MapUtils.isEmpty(ex.getInvalidFields())) {
            throw ex;
        }
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

    public List<FieldTemplate<?>> getFieldTemplates() {
        return fieldTemplates;
    }

    public Map<String, List<FormProcessor>> getActionProcessors() {
        return actionProcessors;
    }

    public void setActionProcessors(Map<String, List<FormProcessor>> actionProcessors) {
        this.actionProcessors = actionProcessors;
    }

    public FieldTemplate<?> getField(String name) {
        return this.getFieldTemplates().stream().filter(field -> field.getName().equals(name)).findFirst().orElse(null);
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