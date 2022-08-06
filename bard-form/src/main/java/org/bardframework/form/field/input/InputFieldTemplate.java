package org.bardframework.form.field.input;

import org.bardframework.form.FieldDescriptionShowType;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;
import org.bardframework.form.exception.FormDataValidationException;
import org.bardframework.form.field.FieldTemplate;
import org.bardframework.form.field.value.FieldValueProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Map;

public abstract class InputFieldTemplate<F extends InputField<T>, T> extends FieldTemplate<F> {

    protected boolean persistentValue = true;
    protected FieldValueProvider<F, T> valueProvider;
    protected FieldDescriptionShowType descriptionShowType;

    protected InputFieldTemplate(String name) {
        super(name);
    }

    public InputFieldTemplate(String name, boolean persistentValue) {
        this(name);
        this.persistentValue = persistentValue;
    }

    public void validate(FormTemplate formTemplate, Object value, Locale locale, HttpServletRequest httpRequest, FormDataValidationException ex)
            throws Exception {
        F formField = this.toField(formTemplate, Map.of(), locale, httpRequest);
        if (Boolean.TRUE.equals(formField.getDisable())) {
            return;
        }
        this.validate(null, formTemplate, formField, (T) value, Map.of(), Map.of(), locale, ex);
    }

    public void validate(String flowToken, FormTemplate formTemplate, Map<String, String> flowData, Map<String, String> formData, Locale locale, HttpServletRequest httpRequest, FormDataValidationException ex)
            throws Exception {
        F formField = this.toField(formTemplate, formData, locale, httpRequest);
        if (Boolean.TRUE.equals(formField.getDisable())) {
            return;
        }
        String stringValue = formData.get(this.getName());
        this.validate(flowToken, formTemplate, formField, this.toValue(stringValue), flowData, formData, locale, ex);
    }

    private void validate(String flowToken, FormTemplate formTemplate, F formField, T value, Map<String, String> flowData, Map<String, String> formData, Locale locale, FormDataValidationException ex)
            throws Exception {
        if (!this.isValid(flowToken, formField, value, flowData)) {
            ex.addFiledError(this.getName(), this.getErrorMessage(formTemplate, formData, locale));
        }
    }

    public abstract boolean isValid(String flowToken, F field, T value, Map<String, String> flowData) throws Exception;

    public abstract T toValue(String value);

    @Override
    public void fill(FormTemplate formTemplate, F field, Map<String, String> values, Locale locale, HttpServletRequest httpRequest) throws Exception {
        super.fill(formTemplate, field, values, locale, httpRequest);
        field.setName(this.getName());
        field.setDescriptionShowType(FormUtils.getFieldEnumProperty(formTemplate, this, "descriptionShowType", FieldDescriptionShowType.class, locale, values, this.getDefaultValues().getDescriptionShowType()));
        field.setPlaceholder(FormUtils.getFieldStringProperty(formTemplate, this, "placeholder", locale, values, this.getDefaultValues().getPlaceholder()));
        field.setRequired(FormUtils.getFieldBooleanProperty(formTemplate, this, "required", locale, values, this.getDefaultValues().getRequired()));
        field.setDisable(FormUtils.getFieldBooleanProperty(formTemplate, this, "disable", locale, values, this.getDefaultValues().getDisable()));
        if (null != valueProvider) {
            field.setValue(valueProvider.getValue(field, httpRequest));
        }
    }

    private String getErrorMessage(FormTemplate formTemplate, Map<String, String> formData, Locale locale) {
        return FormUtils.getFieldStringProperty(formTemplate, this, "errorMessage", locale, formData, this.getDefaultValues().getErrorMessage());
    }

    public boolean isPersistentValue() {
        return persistentValue;
    }

    public void setPersistentValue(boolean persistentValue) {
        this.persistentValue = persistentValue;
    }

    public FieldValueProvider<F, T> getValueProvider() {
        return valueProvider;
    }

    public void setValueProvider(FieldValueProvider<F, T> valueProvider) {
        this.valueProvider = valueProvider;
    }

    public int getValidationOrder() {
        return 0;
    }

    public FieldDescriptionShowType getDescriptionShowType() {
        return descriptionShowType;
    }

    public void setDescriptionShowType(FieldDescriptionShowType descriptionShowType) {
        this.descriptionShowType = descriptionShowType;
    }
}
