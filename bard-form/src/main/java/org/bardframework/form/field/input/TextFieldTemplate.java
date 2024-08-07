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
    public boolean isValid(String flowToken, TextField field, String value, Map<String, String> flowData) {
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
    public void fill(FormTemplate formTemplate, TextField field, Map<String, String> values, Locale locale) throws Exception {
        super.fill(formTemplate, field, values, locale);
        field.setRegex(FormUtils.getFieldStringProperty(formTemplate, this, "regex", locale, values, this.getDefaultValue().getRegex()));
        field.setMask(FormUtils.getFieldStringProperty(formTemplate, this, "mask", locale, values, this.getDefaultValue().getMask()));
        field.setMinLength(FormUtils.getFieldIntegerProperty(formTemplate, this, "minLength", locale, values, this.getDefaultValue().getMinLength()));
        field.setMaxLength(FormUtils.getFieldIntegerProperty(formTemplate, this, "maxLength", locale, values, this.getDefaultValue().getMaxLength()));
    }

    @Override
    public String toValue(String value) {
        return value;
    }
}
