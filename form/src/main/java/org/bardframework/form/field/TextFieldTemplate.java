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
            LOGGER.debug("filed [{}] is required, but it's value is empty", field.getName());
            return false;
        }
        if (null != field.getMinLength() && (null == value ? 0 : value.length()) < field.getMinLength()) {
            LOGGER.debug("filed [{}] min length is [{}], but it's value[{}] length is smaller than minimum", field.getName(), field.getMinLength(), value);
            return false;
        }
        if (null != field.getMaxLength() && (null == value ? 0 : value.length()) > field.getMaxLength()) {
            LOGGER.debug("filed [{}] max length is [{}], but it's value[{}] length is greater than maximum", field.getName(), field.getMaxLength(), value);
            return false;
        }
        if (StringUtils.isNotBlank(field.getRegex()) && (StringUtils.isBlank(value) || !Pattern.matches(field.getRegex(), value))) {
            LOGGER.debug("filed [{}] regex is [{}], but it's value[{}] not match with it", field.getName(), field.getRegex(), value);
            return false;
        }
        return true;
    }

    @Override
    public void fill(FormTemplate formTemplate, TextField field, Map<String, String> args, Locale locale) throws Exception {
        super.fill(formTemplate, field, args, locale);
        field.setRegex(FormUtils.getFieldValue(formTemplate, this.getName(), "regex", locale, args));
        field.setMask(FormUtils.getFieldValue(formTemplate, this.getName(), "mask", locale, args));
        field.setMinLength(FormUtils.getIntegerValue(formTemplate, this.getName(), "minLength", locale, args));
        field.setMaxLength(FormUtils.getIntegerValue(formTemplate, this.getName(), "maxLength", locale, args));
    }

    @Override
    public String toValue(String value) {
        return value;
    }
}
