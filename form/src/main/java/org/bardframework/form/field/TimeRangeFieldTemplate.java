package org.bardframework.form.field;

import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;
import org.bardframework.form.common.field.TimeRangeField;
import org.bardframework.form.common.field.base.FormField;
import org.bardframework.form.common.field.common.RangeValue;
import org.bardframework.form.field.base.FormFieldTemplate;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Map;

public class TimeRangeFieldTemplate extends FormFieldTemplate<TimeRangeField, RangeValue<LocalTime>> {
    private boolean minIsNow;
    private boolean maxIsNow;
    private ChronoUnit lengthUnit = ChronoUnit.SECONDS;

    protected TimeRangeFieldTemplate(String name) {
        super(name);
    }

    @Override
    public TimeRangeField getEmptyField() {
        return new TimeRangeField();
    }

    @Override
    public void fill(FormTemplate formTemplate, TimeRangeField field, Map<String, String> args, Locale locale) throws Exception {
        super.fill(formTemplate, field, args, locale);
        field.setMinLength(FormUtils.getLongValue(formTemplate, this.getName(), "minLength", locale, args));
        field.setMaxLength(FormUtils.getLongValue(formTemplate, this.getName(), "maxLength", locale, args));
        field.setMinValue(FormUtils.getLocalTimeValue(formTemplate, this.getName(), "minValue", locale, args));
        field.setMaxValue(FormUtils.getLocalTimeValue(formTemplate, this.getName(), "maxValue", locale, args));
        field.setLengthUnit(field.getLengthUnit());
        if (null == field.getMinValue()) {
            field.setMinValue(this.getMinValue());
        }
        if (null == field.getMaxValue()) {
            field.setMinValue(this.getMaxValue());
        }
    }

    @Override
    public RangeValue<LocalTime> toValue(String value) {
        String[] parts = value.split(FormField.SEPARATOR);
        if (parts.length != 2) {
            throw new IllegalStateException(value + " is not valid for range value");
        }
        RangeValue<LocalTime> rangeValue = new RangeValue<>();
        if (!parts[0].isBlank()) {
            rangeValue.setFrom(LocalTime.parse(parts[0]));
        }
        if (!parts[1].isBlank()) {
            rangeValue.setTo(LocalTime.parse(parts[1]));
        }
        return rangeValue;
    }

    @Override
    public boolean isValid(TimeRangeField field, RangeValue<LocalTime> value) {
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
        long length = (null == value || null == value.getFrom() || null == value.getTo()) ? Long.MAX_VALUE : Duration.between(value.getFrom(), value.getTo()).get(this.getLengthUnit()) + 1;
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

    public LocalTime getMinValue() {
        return minIsNow ? LocalTime.now() : null;
    }

    public LocalTime getMaxValue() {
        return maxIsNow ? LocalTime.now() : null;
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

    public ChronoUnit getLengthUnit() {
        return lengthUnit;
    }

    public void setLengthUnit(ChronoUnit lengthUnit) {
        this.lengthUnit = lengthUnit;
    }
}