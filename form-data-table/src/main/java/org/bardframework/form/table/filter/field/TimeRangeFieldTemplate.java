package org.bardframework.form.table.filter.field;

import org.apache.commons.lang3.StringUtils;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;
import org.bardframework.form.common.FormField;
import org.bardframework.form.field.base.FormFieldTemplate;
import org.bardframework.form.filter.TimeFilter;
import org.bardframework.form.filter.field.TimeFilterField;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Map;

public class TimeRangeFieldTemplate extends FormFieldTemplate<TimeFilterField, TimeFilter> {
    private boolean minIsNow;
    private boolean maxIsNow;
    private ChronoUnit lengthUnit = ChronoUnit.SECONDS;

    protected TimeRangeFieldTemplate(String name) {
        super(name);
    }

    @Override
    public void fill(FormTemplate formTemplate, TimeFilterField field, Map<String, String> args, Locale locale) throws Exception {
        super.fill(formTemplate, field, args, locale);
        field.setMinLength(FormUtils.getFieldLongProperty(formTemplate, this.getName(), "minLength", locale, args, null));
        field.setMaxLength(FormUtils.getFieldLongProperty(formTemplate, this.getName(), "maxLength", locale, args, null));
        field.setMinValue(FormUtils.getFieldLocalTimeProperty(formTemplate, this.getName(), "minValue", locale, args, null));
        field.setMaxValue(FormUtils.getFieldLocalTimeProperty(formTemplate, this.getName(), "maxValue", locale, args, null));
        field.setLengthUnit(field.getLengthUnit());
        if (null == field.getMinValue()) {
            field.setMinValue(this.getMinValue());
        }
        if (null == field.getMaxValue()) {
            field.setMinValue(this.getMaxValue());
        }
    }

    @Override
    public TimeFilter toValue(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        String[] parts = value.split(FormField.SEPARATOR);
        if (parts.length != 2) {
            throw new IllegalStateException(value + " is not valid for range value");
        }
        TimeFilter filter = new TimeFilter();
        if (!parts[0].isBlank()) {
            filter.setFrom(LocalTime.parse(parts[0]));
        }
        if (!parts[1].isBlank()) {
            filter.setTo(LocalTime.parse(parts[1]));
        }
        return filter;
    }

    @Override
    public boolean isValid(TimeFilterField field, TimeFilter value) {
        if ((null == value || null == value.getFrom() || null == value.getTo()) && Boolean.TRUE.equals(field.getDisable())) {
            LOGGER.debug("field [{}] is required, but it's value is empty", field.getName());
            return false;
        }
        if (null != field.getMinValue()) {
            if (null != value && null != value.getFrom() && value.getFrom().isBefore(this.getMinValue())) {
                LOGGER.debug("field [{}] min value is [{}], but it's value is less than minimum", field.getName(), field.getMinValue());
                return false;
            }
            if (null != value && null != value.getTo() && value.getTo().isBefore(this.getMinValue())) {
                LOGGER.debug("field [{}] min value is [{}], but it's value is less than minimum", field.getName(), field.getMinValue());
                return false;
            }
        }
        if (null != field.getMaxValue()) {
            if (null != value && null != value.getFrom() && value.getFrom().isAfter(this.getMaxValue())) {
                LOGGER.debug("field [{}] max value is [{}], but it's value is greater than maximum", field.getName(), field.getMaxValue());
                return false;
            }
            if (null != value && null != value.getTo() && value.getTo().isAfter(this.getMaxValue())) {
                LOGGER.debug("field [{}] max value is [{}], but it's value is greater than maximum", field.getName(), field.getMaxValue());
                return false;
            }
        }
        long length = (null == value || null == value.getFrom() || null == value.getTo()) ? Long.MAX_VALUE : Duration.between(value.getFrom(), value.getTo()).get(this.getLengthUnit()) + 1;
        if (length < 0) {
            LOGGER.debug("values[{}] of range field[{}] is invalid, from is greater than to", value, field.getName());
            /*
                from > to
             */
            return false;
        }
        if (null != field.getMinLength() && length < field.getMinLength()) {
            LOGGER.debug("field [{}] min length is [{}], but it's value length is smaller than minimum", field.getName(), field.getMaxLength());
            return false;
        }
        if (null != field.getMaxLength() && length > field.getMaxLength()) {
            LOGGER.debug("field [{}] max length is [{}], but it's value length is greater than maximum", field.getName(), field.getMaxLength());
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