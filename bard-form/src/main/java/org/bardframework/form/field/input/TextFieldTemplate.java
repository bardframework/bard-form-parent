package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;

import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

@Getter
@Setter
public class TextFieldTemplate extends InputFieldTemplateAbstract<TextField, String> {

    public TextFieldTemplate(String name) {
        super(name);
    }

    @Override
    public boolean isValid(String flowToken, TextField field, String value, Map<String, Object> flowData) {
        if (StringUtils.isBlank(value)) {
            if (Boolean.TRUE.equals(field.getRequired())) {
                log.debug("field [{}] is required, but it's value is empty", field.getName());
                return false;
            }
            return true;
        }
        if (null != field.getMinLength() && value.length() < field.getMinLength()) {
            log.debug("field [{}] min length is [{}], but it's value[{}] length is smaller than minimum", field.getName(), field.getMinLength(), value);
            return false;
        }
        if (null != field.getMaxLength() && value.length() > field.getMaxLength()) {
            log.debug("field [{}] max length is [{}], but it's value[{}] length is greater than maximum", field.getName(), field.getMaxLength(), value);
            return false;
        }
        if (StringUtils.isNotBlank(field.getRegex()) && !Pattern.matches(field.getRegex(), value)) {
            log.debug("field [{}] regex is [{}], but it's value[{}] not match with it", field.getName(), field.getRegex(), value);
            return false;
        }
        return true;
    }

    @Override
    public void fill(FormTemplate formTemplate, TextField field, Map<String, Object> values, Map<String, Object> args, Locale locale) throws Exception {
        super.fill(formTemplate, field, values, args, locale);
        field.setRegex(FormUtils.getFieldStringProperty(formTemplate, this, "regex", locale, args, this.getDefaultValue().getRegex()));
        field.setMask(FormUtils.getFieldStringProperty(formTemplate, this, "mask", locale, args, this.getDefaultValue().getMask()));
        field.setMinLength(FormUtils.getFieldIntegerProperty(formTemplate, this, "minLength", locale, args, this.getDefaultValue().getMinLength()));
        field.setMaxLength(FormUtils.getFieldIntegerProperty(formTemplate, this, "maxLength", locale, args, this.getDefaultValue().getMaxLength()));
    }
}
