package org.bardframework.form.field.input;

import org.apache.commons.lang3.StringUtils;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public class NewPasswordFieldTemplate extends PasswordFieldTemplate {

    public NewPasswordFieldTemplate(String name) {
        super(name);
    }

    @Override
    public boolean isValid(PasswordField field, String value, Map<String, String> flowData) {
        if (StringUtils.isBlank(value)) {
            if (Boolean.TRUE.equals(field.getRequired())) {
                LOGGER.debug("field [{}] is required, but it's value is empty", field.getName());
                return false;
            }
            return true;
        }
        if (null != field.getMinLength() && value.length() < field.getMinLength()) {
            LOGGER.debug("field [{}] min length is [{}], but it's value[{}] length is smaller than minimum", field.getName(), field.getMinLength(), value);
            return false;
        }
        if (null != field.getMaxLength() && value.length() > field.getMaxLength()) {
            LOGGER.debug("field [{}] max length is [{}], but it's value[{}] length is greater than maximum", field.getName(), field.getMaxLength(), value);
            return false;
        }
        if (StringUtils.isNotBlank(field.getRegex()) && !Pattern.matches(field.getRegex(), value)) {
            LOGGER.debug("field [{}] regex is [{}], but it's value[{}] not match with it", field.getName(), field.getRegex(), value);
            return false;
        }
        return true;
    }

    @Override
    public void fill(FormTemplate formTemplate, PasswordField field, Map<String, String> values, Locale locale, HttpServletRequest httpRequest) throws Exception {
        super.fill(formTemplate, field, values, locale, httpRequest);
        field.setRegex(FormUtils.getFieldStringProperty(formTemplate, this, "regex", locale, values, this.getDefaultValues().getRegex()));
        field.setMinLength(FormUtils.getFieldIntegerProperty(formTemplate, this, "minLength", locale, values, this.getDefaultValues().getMinLength()));
        field.setMaxLength(FormUtils.getFieldIntegerProperty(formTemplate, this, "maxLength", locale, values, this.getDefaultValues().getMaxLength()));
    }

    @Override
    protected NewPasswordField getEmptyField() {
        return new NewPasswordField();
    }
}
