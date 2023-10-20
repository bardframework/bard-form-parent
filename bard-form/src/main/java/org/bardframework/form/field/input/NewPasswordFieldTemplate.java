package org.bardframework.form.field.input;

import org.apache.commons.lang3.StringUtils;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;

import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public class NewPasswordFieldTemplate extends PasswordFieldTemplate {

    public NewPasswordFieldTemplate(String name) {
        super(name);
    }

    @Override
    public boolean isValid(String flowToken, PasswordField field, String value, Map<String, String> flowData) {
        if (StringUtils.isBlank(value)) {
            if (Boolean.TRUE.equals(field.getRequired())) {
                log.debug("field [{}] is required, but it's value is empty", field.getName());
                return false;
            }
            return true;
        }
        byte[] passwordByteArray = Base64.getDecoder().decode(value);
        String rawPassword = new String(passwordByteArray, Charset.defaultCharset());
        if (null != field.getMinLength() && rawPassword.length() < field.getMinLength()) {
            log.debug("field [{}] min length is [{}], but it's value[{}] length is smaller than minimum", field.getName(), field.getMinLength(), rawPassword);
            return false;
        }
        if (null != field.getMaxLength() && rawPassword.length() > field.getMaxLength()) {
            log.debug("field [{}] max length is [{}], but it's value[{}] length is greater than maximum", field.getName(), field.getMaxLength(), rawPassword);
            return false;
        }
        if (StringUtils.isNotBlank(field.getRegex()) && !Pattern.matches(field.getRegex(), rawPassword)) {
            log.debug("field [{}] regex is [{}], but it's value[{}] not match with it", field.getName(), field.getRegex(), rawPassword);
            return false;
        }
        return true;
    }

    @Override
    public void fill(FormTemplate formTemplate, PasswordField field, Map<String, String> values, Locale locale) throws Exception {
        super.fill(formTemplate, field, values, locale);
        field.setRegex(FormUtils.getFieldStringProperty(formTemplate, this, "regex", locale, values, this.getDefaultValues().getRegex()));
        field.setMinLength(FormUtils.getFieldIntegerProperty(formTemplate, this, "minLength", locale, values, this.getDefaultValues().getMinLength()));
        field.setMaxLength(FormUtils.getFieldIntegerProperty(formTemplate, this, "maxLength", locale, values, this.getDefaultValues().getMaxLength()));
    }

    @Override
    protected NewPasswordField getEmptyField() {
        return new NewPasswordField();
    }
}
