package org.bardframework.form.field;

import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;
import org.bardframework.form.common.field.NumberRangeField;
import org.bardframework.form.common.field.base.FormField;
import org.bardframework.form.common.field.common.RangeValue;
import org.bardframework.form.field.base.FormFieldTemplate;

import java.util.Locale;
import java.util.Map;

public class NumberRangeFieldTemplate extends FormFieldTemplate<NumberRangeField, RangeValue<Long>> {

    protected NumberRangeFieldTemplate(String name) {
        super(name);
    }

    @Override
    public boolean isValid(NumberRangeField field, RangeValue<Long> value) {
        if ((null == value || null == value.getFrom() || null == value.getTo()) && Boolean.TRUE.equals(field.getDisable())) {
            LOGGER.debug("filed [{}] is required, but it's value is empty", field.getName());
            return false;
        }
        if (null != field.getMinValue()) {
            if (null != value.getFrom() && value.getFrom() < field.getMinValue()) {
                LOGGER.debug("filed [{}] min value is [{}], but it's value is less than minimum", field.getName(), field.getMinValue());
                return false;
            }
            if (null != value.getTo() && value.getTo() < field.getMinValue()) {
                LOGGER.debug("filed [{}] min value is [{}], but it's value is less than minimum", field.getName(), field.getMinValue());
                return false;
            }
        }
        if (null != field.getMaxValue()) {
            if (null != value.getFrom() && value.getFrom() > field.getMaxValue()) {
                LOGGER.debug("filed [{}] max value is [{}], but it's value is greater than maximum", field.getName(), field.getMaxValue());
                return false;
            }
            if (null != value.getTo() && value.getTo() > field.getMaxValue()) {
                LOGGER.debug("filed [{}] max value is [{}], but it's value is greater than maximum", field.getName(), field.getMaxValue());
                return false;
            }
        }
        long length = (null == value.getFrom() || null == value.getTo()) ? Long.MAX_VALUE : value.getTo() - value.getFrom();
        if (length < 0) {
            LOGGER.debug("values[{}] of range filed[{}] is invalid, from is greater than to", value, field.getName());
            /*
                from > to
             */
            return false;
        }
        if (null != field.getMinLength() && length < field.getMinLength()) {
            LOGGER.debug("filed [{}] min length is [{}], but it's value length is smaller than minimum", field.getName(), field.getMaxLength());
            return false;
        }
        if (null != field.getMaxLength() && length > field.getMaxLength()) {
            LOGGER.debug("filed [{}] max length is [{}], but it's value length is greater than maximum", field.getName(), field.getMaxLength());
            return false;
        }
        return true;
    }

    @Override
    public void fill(FormTemplate formTemplate, NumberRangeField field, Map<String, String> args, Locale locale) throws Exception {
        super.fill(formTemplate, field, args, locale);
        field.setMinLength(FormUtils.getLongValue(formTemplate, this.getName(), "minLength", locale, args));
        field.setMaxLength(FormUtils.getLongValue(formTemplate, this.getName(), "maxLength", locale, args));
        field.setMinValue(FormUtils.getLongValue(formTemplate, this.getName(), "minValue", locale, args));
        field.setMaxValue(FormUtils.getLongValue(formTemplate, this.getName(), "maxValue", locale, args));
    }


    @Override
    public RangeValue<Long> toValue(String value) {
        String[] parts = value.split(FormField.SEPARATOR);
        if (parts.length != 2) {
            throw new IllegalStateException(value + " is not valid for range value");
        }
        RangeValue<Long> rangeValue = new RangeValue<>();
        if (!parts[0].isBlank()) {
            rangeValue.setFrom(Long.valueOf(parts[0]));
        }
        if (!parts[1].isBlank()) {
            rangeValue.setTo(Long.valueOf(parts[1]));
        }
        return rangeValue;
    }
}