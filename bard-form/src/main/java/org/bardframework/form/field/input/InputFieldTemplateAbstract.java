package org.bardframework.form.field.input;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import org.bardframework.form.FieldDescriptionShowType;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;
import org.bardframework.form.exception.FormDataValidationException;
import org.bardframework.form.field.FieldTemplate;
import org.bardframework.form.field.value.FieldValueProvider;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.SpelCompilerMode;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Locale;
import java.util.Map;

@Getter
@Setter
public abstract class InputFieldTemplateAbstract<F extends InputField<T>, T> extends FieldTemplate<F> {

    protected boolean persistentValue = true;
    protected FieldValueProvider<F, T> valueProvider;
    protected Expression disableExpression = null;

    public InputFieldTemplateAbstract(String name) {
        super(name);
    }

    public InputFieldTemplateAbstract(String name, boolean persistentValue) {
        this(name);
        this.persistentValue = persistentValue;
    }

    public void validate(FormTemplate formTemplate, Map<String, Object> values, Object value, Locale locale, HttpServletRequest httpRequest, FormDataValidationException ex)
            throws Exception {
        F formField = this.toField(formTemplate, values, Map.of(), locale);
        if (Boolean.TRUE.equals(formField.getDisable())) {
            return;
        }
        this.validate(null, formTemplate, formField, (T) value, Map.of(), Map.of(), locale, ex);
    }

    public void validate(String flowToken, FormTemplate formTemplate, Map<String, Object> flowData, Map<String, Object> formData, Locale locale, FormDataValidationException ex)
            throws Exception {
        F formField = this.toField(formTemplate, flowData, formData, locale);
        if (Boolean.TRUE.equals(formField.getDisable())) {
            return;
        }
        Object value = formData.get(this.getName());
        T cleanValue = this.toValue(value);
        this.validate(flowToken, formTemplate, formField, cleanValue, flowData, formData, locale, ex);
    }

    private void validate(String flowToken, FormTemplate formTemplate, F formField, T value, Map<String, Object> flowData, Map<String, Object> formData, Locale locale, FormDataValidationException ex)
            throws Exception {
        if (!this.isValid(flowToken, formField, value, flowData)) {
            ex.addFieldError(this.getName(), this.getErrorMessage(formTemplate, formData, locale));
        }
    }

    public abstract boolean isValid(String flowToken, F field, T value, Map<String, Object> flowData) throws Exception;

    public T toValue(Object value) {
        return (T) value;
    }

    @Override
    public void fill(FormTemplate formTemplate, F field, Map<String, Object> values, Map<String, Object> args, Locale locale) throws Exception {
        super.fill(formTemplate, field, values, args, locale);
        field.setDescriptionShowType(FormUtils.getFieldEnumProperty(formTemplate, this, "descriptionShowType", FieldDescriptionShowType.class, locale, args, this.getDefaultValue().getDescriptionShowType()));
        field.setPlaceholder(FormUtils.getFieldStringProperty(formTemplate, this, "placeholder", locale, args, this.getDefaultValue().getPlaceholder()));
        field.setErrorMessage(FormUtils.getFieldStringProperty(formTemplate, this, "errorMessage", locale, args, this.getDefaultValue().getErrorMessage()));
        field.setRequired(FormUtils.getFieldBooleanProperty(formTemplate, this, "required", locale, args, this.getDefaultValue().getRequired()));
        if (null == disableExpression) {
            field.setDisable(FormUtils.getFieldBooleanProperty(formTemplate, this, "disable", locale, args, this.getDefaultValue().getDisable()));
        } else {
            field.setDisable(Boolean.TRUE.equals(this.disableExpression.getValue(new StandardEvaluationContext(args), Boolean.class)));
        }
        if (null != valueProvider) {
            field.setValue(valueProvider.getValue(field));
        }
    }

    public String getErrorMessage(FormTemplate formTemplate, Map<String, Object> formData, Locale locale) {
        return FormUtils.getFieldStringProperty(formTemplate, this, "errorMessage", locale, formData, this.getDefaultValue().getErrorMessage());
    }

    public int getValidationOrder() {
        return 0;
    }

    public void setDisableExpression(String disableExpression) {
        this.disableExpression = (new SpelExpressionParser(new SpelParserConfiguration(SpelCompilerMode.MIXED, null))).parseExpression(disableExpression);
    }
}
