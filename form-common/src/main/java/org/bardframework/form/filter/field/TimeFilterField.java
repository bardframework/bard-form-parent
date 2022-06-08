package org.bardframework.form.filter.field;

import org.bardframework.form.common.FormField;
import org.bardframework.form.filter.FilterFieldType;
import org.bardframework.form.filter.TimeFilter;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class TimeFilterField extends FormField<TimeFilter> {
    private Long minLength;
    private Long maxLength;
    private ChronoUnit lengthUnit;
    private LocalTime minValue;
    private LocalTime maxValue;

    public TimeFilterField() {
    }

    public TimeFilterField(String name) {
        super(name);
    }

    @Override
    public FilterFieldType getType() {
        return FilterFieldType.TIME_FILTER;
    }

    @Override
    public String toString(TimeFilter value) {
        return String.join(SEPARATOR, null == value.getFrom() ? "" : value.getFrom().toString(), null == value.getTo() ? "" : value.getTo().toString());
    }

    public Long getMinLength() {
        return minLength;
    }

    public void setMinLength(Long minLength) {
        this.minLength = minLength;
    }

    public Long getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Long maxLength) {
        this.maxLength = maxLength;
    }

    public LocalTime getMinValue() {
        return minValue;
    }

    public void setMinValue(LocalTime minValue) {
        this.minValue = minValue;
    }

    public LocalTime getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(LocalTime maxValue) {
        this.maxValue = maxValue;
    }

    public ChronoUnit getLengthUnit() {
        return lengthUnit;
    }

    public void setLengthUnit(ChronoUnit lengthUnit) {
        this.lengthUnit = lengthUnit;
    }
}