package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;

import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

@Getter
@Setter
public class NewPasswordFieldTemplate extends InputFieldTemplateAbstract<NewPasswordField, String> {

    public NewPasswordFieldTemplate(String name) {
        super(name);
    }

    @Override
    public boolean isValid(String flowToken, NewPasswordField field, String value, Map<String, Object> flowData) {
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
    public void fill(FormTemplate formTemplate, NewPasswordField field, Map<String, Object> values, Map<String, Object> args, Locale locale) throws Exception {
        super.fill(formTemplate, field, values, args, locale);
        field.setShowConfirmPassword(FormUtils.getFieldBooleanProperty(formTemplate, this, "showConfirmPassword", locale, args, this.getDefaultValue().getShowConfirmPassword()));
        field.setRegex(FormUtils.getFieldStringProperty(formTemplate, this, "regex", locale, args, this.getDefaultValue().getRegex()));
        field.setMinLength(FormUtils.getFieldIntegerProperty(formTemplate, this, "minLength", locale, args, this.getDefaultValue().getMinLength()));
        field.setMaxLength(FormUtils.getFieldIntegerProperty(formTemplate, this, "maxLength", locale, args, this.getDefaultValue().getMaxLength()));
    }

    @Override
    protected NewPasswordField getEmptyField() {
        return new NewPasswordField();
    }
}
