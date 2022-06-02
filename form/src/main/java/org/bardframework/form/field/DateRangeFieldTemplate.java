package org.bardframework.form.field;

import org.apache.commons.lang3.StringUtils;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;
import org.bardframework.form.common.field.DateRangeField;
import org.bardframework.form.common.field.base.FormField;
import org.bardframework.form.common.field.common.RangeValue;
import org.bardframework.form.field.base.FormFieldTemplate;

import java.time.LocalDate;
import java.time.Period;
import java.util.Locale;
import java.util.Map;

public class DateRangeFieldTemplate extends FormFieldTemplate<DateRangeField, RangeValue<LocalDate>> {
    private boolean minIsNow;
    private boolean maxIsNow;

    protected DateRangeFieldTemplate(String name) {
        super(name);
    }

    @Override
    public void fill(FormTemplate formTemplate, DateRangeField field, Map<String, String> args, Locale locale) throws Exception {
        super.fill(formTemplate, field, args, locale);
        field.setMinLength(FormUtils.getFieldLongProperty(formTemplate, this.getName(), "minLength", locale, args, null));
        field.setMaxLength(FormUtils.getFieldLongProperty(formTemplate, this.getName(), "maxLength", locale, args, null));
        field.setMinValue(FormUtils.getFieldLocalDateProperty(formTemplate, this.getName(), "minValue", locale, args, null));
        field.setMaxValue(FormUtils.getFieldLocalDateProperty(formTemplate, this.getName(), "maxValue", locale, args, null));
        if (null == field.getMinValue()) {
            field.setMinValue(this.getMinValue());
        }
        if (null == field.getMaxValue()) {
            field.setMinValue(this.getMaxValue());
        }
    }

    @Override
    public RangeValue<LocalDate> toValue(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        String[] parts = value.split(FormField.SEPARATOR);
        if (parts.length != 2) {
            throw new IllegalStateException(value + " is not valid for range value");
        }
        RangeValue<LocalDate> rangeValue = new RangeValue<>();
        if (!parts[0].isBlank()) {
            rangeValue.setFrom(LocalDate.parse(parts[0]));
        }
        if (!parts[1].isBlank()) {
            rangeValue.setTo(LocalDate.parse(parts[1]));
        }
        return rangeValue;
    }

    @Override
    public boolean isValid(DateRangeField field, RangeValue<LocalDate> value) {
        if ((null == value || null == value.getFrom() || null == value.getTo()) && Boolean.TRUE.equals(field.getDisable())) {
            LOGGER.debug("filed [{}] is required, but it's value is empty", field.getName());
            return false;
        }
        if (null != field.getMinValue()) {
            if (null != value && null != value.getFrom() && value.getFrom().isBefore(this.getMinValue())) {
                LOGGER.debug("filed [{}] min value is [{}], but it's value is less than minimum", field.getName(), field.getMinValue());
                return false;
            }
            if (null != value && null != value.getTo() && value.getTo().isBefore(this.getMinValue())) {
                LOGGER.debug("filed [{}] min value is [{}], but it's value is less than minimum", field.getName(), field.getMinValue());
                return false;
            }
        }
        if (null != field.getMaxValue()) {
            if (null != value && null != value.getFrom() && value.getFrom().isAfter(this.getMaxValue())) {
                LOGGER.debug("filed [{}] max value is [{}], but it's value is greater than maximum", field.getName(), field.getMaxValue());
                return false;
            }
            if (null != value && null != value.getTo() && value.getTo().isAfter(this.getMaxValue())) {
                LOGGER.debug("filed [{}] max value is [{}], but it's value is greater than maximum", field.getName(), field.getMaxValue());
                return false;
            }
        }
        long length = (null == value || null == value.getFrom() || null == value.getTo()) ? Long.MAX_VALUE : Period.between(value.getFrom(), value.getTo()).getDays() + 1;
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

    public LocalDate getMinValue() {
        return minIsNow ? LocalDate.now() : null;
    }

    public LocalDate getMaxValue() {
        return maxIsNow ? LocalDate.now() : null;
    }

    public boolean isMinIsNow() {
        return minIsNow;
    }

    public void setMinIsNow(boolean minIsNow) {
        this.minIsNow = minIsNow;
    }

    public boolean isMaxIsNow() {
        return maxIsNow;
    }

    public void setMaxIsNow(boolean maxIsNow) {
        this.maxIsNow = maxIsNow;
    }
}