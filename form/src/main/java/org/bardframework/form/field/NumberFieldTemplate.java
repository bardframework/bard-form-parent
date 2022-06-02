package org.bardframework.form.field;

import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;
import org.bardframework.form.common.field.NumberField;
import org.bardframework.form.field.base.FormFieldTemplate;

import java.util.Locale;
import java.util.Map;

public class NumberFieldTemplate extends FormFieldTemplate<NumberField, Long> {

    protected NumberFieldTemplate(String name) {
        super(name);
    }

    @Override
    public boolean isValid(NumberField field, Long value) {
        if (null == value && Boolean.TRUE.equals(field.getDisable())) {
            LOGGER.debug("filed [{}] is required, but it's value is empty", field.getName());
            return false;
        }
        if (null != field.getMinValue() && (value == null || value < field.getMinValue())) {
            LOGGER.debug("filed [{}] min value is [{}], but it's value is less than minimum", field.getName(), field.getMinValue());
            return false;
        }
        if (null != field.getMaxValue() && (value == null || value > field.getMaxValue())) {
            LOGGER.debug("filed [{}] max value is [{}], but it's value is greater than maximum", field.getName(), field.getMaxValue());
            return false;
        }
        return true;
    }

    @Override
    public void fill(FormTemplate formTemplate, NumberField field, Map<String, String> args, Locale locale) throws Exception {
        super.fill(formTemplate, field, args, locale);
        field.setMinValue(FormUtils.getLongValue(formTemplate, this.getName(), "minValue", locale, args));
        field.setMaxValue(FormUtils.getLongValue(formTemplate, this.getName(), "maxValue", locale, args));
        field.setMask(FormUtils.getFieldValue(formTemplate, this.getName(), "mask", locale, args));
    }

    @Override
    public Long toValue(String value) {
        return Long.valueOf(value);
    }

    public String toString(Long value) {
        return null == value ? null : value.toString();
    }
}