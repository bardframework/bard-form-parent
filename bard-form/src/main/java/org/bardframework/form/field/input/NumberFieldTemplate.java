package org.bardframework.form.field.input;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;

import java.util.Locale;
import java.util.Map;

@Slf4j
public class NumberFieldTemplate extends InputFieldTemplate<NumberField, Long> {

    protected NumberFieldTemplate(String name) {
        super(name);
    }

    @Override
    public boolean isValid(String flowToken, NumberField field, Long value, Map<String, String> flowData) {
        if (null == value) {
            if (Boolean.TRUE.equals(field.getRequired())) {
                log.debug("field [{}] is required, but it's value is empty", field.getName());
                return false;
            }
            return true;
        }
        if (null != field.getMinValue() && value < field.getMinValue()) {
            log.debug("field [{}] min value is [{}], but it's value is less than minimum", field.getName(), field.getMinValue());
            return false;
        }
        if (null != field.getMaxValue() && value > field.getMaxValue()) {
            log.debug("field [{}] max value is [{}], but it's value is greater than maximum", field.getName(), field.getMaxValue());
            return false;
        }
        return true;
    }

    @Override
    public void fill(FormTemplate formTemplate, NumberField field, Map<String, String> values, Locale locale) throws Exception {
        super.fill(formTemplate, field, values, locale);
        field.setMinValue(FormUtils.getFieldLongProperty(formTemplate, this, "minValue", locale, values, this.getDefaultValues().getMinValue()));
        field.setMaxValue(FormUtils.getFieldLongProperty(formTemplate, this, "maxValue", locale, values, this.getDefaultValues().getMaxValue()));
        field.setMask(FormUtils.getFieldStringProperty(formTemplate, this, "mask", locale, values, this.getDefaultValues().getMask()));
    }

    @Override
    public Long toValue(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return Long.valueOf(value);
    }

    public String toString(Long value) {
        return null == value ? null : value.toString();
    }
}