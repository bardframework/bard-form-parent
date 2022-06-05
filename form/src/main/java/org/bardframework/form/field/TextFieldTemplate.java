package org.bardframework.form.field;

import org.apache.commons.lang3.StringUtils;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;
import org.bardframework.form.common.field.TextField;
import org.bardframework.form.field.base.FormFieldTemplate;

import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public class TextFieldTemplate extends FormFieldTemplate<TextField, String> {

    protected TextFieldTemplate(String name) {
        super(name);
    }

    @Override
    public boolean isValid(TextField field, String value) {
        if (StringUtils.isBlank(value) && Boolean.TRUE.equals(field.getDisable())) {
            LOGGER.debug("field [{}] is required, but it's value is empty", field.getName());
            return false;
        }
        if (null != field.getMinLength() && (null == value ? 0 : value.length()) < field.getMinLength()) {
            LOGGER.debug("field [{}] min length is [{}], but it's value[{}] length is smaller than minimum", field.getName(), field.getMinLength(), value);
            return false;
        }
        if (null != field.getMaxLength() && (null == value ? 0 : value.length()) > field.getMaxLength()) {
            LOGGER.debug("field [{}] max length is [{}], but it's value[{}] length is greater than maximum", field.getName(), field.getMaxLength(), value);
            return false;
        }
        if (StringUtils.isNotBlank(field.getRegex()) && (StringUtils.isBlank(value) || !Pattern.matches(field.getRegex(), value))) {
            LOGGER.debug("field [{}] regex is [{}], but it's value[{}] not match with it", field.getName(), field.getRegex(), value);
            return false;
        }
        return true;
    }

    @Override
    public void fill(FormTemplate formTemplate, TextField field, Map<String, String> args, Locale locale) throws Exception {
        super.fill(formTemplate, field, args, locale);
        field.setRegex(FormUtils.getFieldStringProperty(formTemplate, this.getName(), "regex", locale, args, null));
        field.setMask(FormUtils.getFieldStringProperty(formTemplate, this.getName(), "mask", locale, args, null));
        field.setMinLength(FormUtils.getFieldIntegerProperty(formTemplate, this.getName(), "minLength", locale, args, null));
        field.setMaxLength(FormUtils.getFieldIntegerProperty(formTemplate, this.getName(), "maxLength", locale, args, null));
    }

    @Override
    public String toValue(String value) {
        return value;
    }
}
