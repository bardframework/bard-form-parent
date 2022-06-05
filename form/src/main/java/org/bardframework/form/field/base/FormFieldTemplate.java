package org.bardframework.form.field.base;

import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;
import org.bardframework.form.common.field.base.FormField;
import org.bardframework.form.field.value.FieldValueProvider;
import org.bardframework.form.processor.FormProcessor;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public abstract class FormFieldTemplate<F extends FormField<T>, T> extends FieldTemplate<F> implements WithValueFieldTemplate<T> {

    protected boolean persistentValue = true;
    protected FieldValueProvider<F, T> valueProvider;
    protected List<FormProcessor> preProcessors;
    protected List<FormProcessor> postProcessors;
    protected Map<String, List<FormProcessor>> actionProcessors = new HashMap<>();

    protected FormFieldTemplate(String name) {
        super(name);
    }

    public FormFieldTemplate(String name, boolean persistentValue) {
        this(name);
        this.persistentValue = persistentValue;
    }

    public abstract boolean isValid(F field, T value);

    @Override
    public abstract T toValue(String value);

    @Override
    public void fill(FormTemplate formTemplate, F field, Map<String, String> args, Locale locale) throws Exception {
        super.fill(formTemplate, field, args, locale);
        field.setName(this.getName());
        field.setPlaceholder(FormUtils.getFieldStringProperty(formTemplate, this.getName(), "placeholder", locale, args, null));
        field.setTooltip(FormUtils.getFieldStringProperty(formTemplate, this.getName(), "tooltip", locale, args, null));
        field.setRequired(FormUtils.getFieldBooleanProperty(formTemplate, this.getName(), "required", locale, args, null));
        field.setFocused(FormUtils.getFieldBooleanProperty(formTemplate, this.getName(), "focused", locale, args, null));
        field.setDisable(FormUtils.getFieldBooleanProperty(formTemplate, this.getName(), "disable", locale, args, null));
        field.setErrorMessage(FormUtils.getFieldStringProperty(formTemplate, this.getName(), "errorMessage", locale, args, null));
        if (null != valueProvider) {
            field.setValue(valueProvider.getValue(field));
        }
    }

    public boolean isPersistentValue() {
        return persistentValue;
    }

    public void setPersistentValue(boolean persistentValue) {
        this.persistentValue = persistentValue;
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

    public FieldValueProvider<F, T> getValueProvider() {
        return valueProvider;
    }

    public void setValueProvider(FieldValueProvider<F, T> valueProvider) {
        this.valueProvider = valueProvider;
    }
}
