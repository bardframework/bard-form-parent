package org.bardframework.form.field.input;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;

import java.util.Locale;
import java.util.Map;

abstract class IpFieldTemplate<F extends Ip4Field> extends InputFieldTemplate<F, String> {

    protected IpFieldTemplate(String name) {
        super(name);
    }

    @Override
    public boolean isValid(String flowToken, F field, String value, Map<String, String> flowData) {
        if (StringUtils.isBlank(value)) {
            if (Boolean.TRUE.equals(field.getRequired())) {
                log.debug("field [{}] is required, but it's value is empty", field.getName());
                return false;
            }
            return true;
        }
        if (!NumberUtils.isDigits(value)) {
            log.debug("field [{}] value[{}] contains invalid characters, only numbers are acceptable.", field.getName(), value);
            return false;
        }
        if (value.length() != this.getIpLength()) {
            log.debug("field [{}] value[{}] length not is invalid, it's length must be {}.", field.getName(), value, this.getIpLength());
            return false;
        }
        String[] parts = value.split("(?<=\\G.{3})");
        for (String part : parts) {
            int partAsNumber = Integer.parseInt(part);
            if (partAsNumber < 0 || partAsNumber > 256) {
                log.debug("field [{}] value[{}] is not a valid ip, [{}] not acceptable.", field.getName(), value, part);
                return false;
            }
        }
        if (null != field.getMinValue() && value.compareTo(field.getMinValue()) < 0) {
            log.debug("field [{}] min value is [{}], but it's value is less than minimum", field.getName(), field.getMinValue());
            return false;
        }
        if (null != field.getMaxValue() && value.compareTo(field.getMaxValue()) > 0) {
            log.debug("field [{}] max value is [{}], but it's value is greater than maximum", field.getName(), field.getMaxValue());
            return false;
        }
        return true;
    }

    @Override
    public void fill(FormTemplate formTemplate, F field, Map<String, String> values, Locale locale) throws Exception {
        super.fill(formTemplate, field, values, locale);
        field.setMinValue(FormUtils.getFieldStringProperty(formTemplate, this, "minValue", locale, values, this.getDefaultValues().getMinValue()));
        field.setMaxValue(FormUtils.getFieldStringProperty(formTemplate, this, "maxValue", locale, values, this.getDefaultValues().getMaxValue()));
    }

    @Override
    public String toValue(String value) {
        return value;
    }

    protected abstract int getIpLength();
}