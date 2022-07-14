package org.bardframework.form.field.filter;

import org.bardframework.form.field.FieldType;
import org.bardframework.form.field.input.InputField;
import org.bardframework.form.model.filter.LocalDateTimeFilter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class DateTimeFilterField extends InputField<LocalDateTimeFilter> {
    private Long minLength;
    private Long maxLength;
    private ChronoUnit lengthUnit;
    private LocalDateTime minValue;
    private LocalDateTime maxValue;

    public DateTimeFilterField() {
    }

    public DateTimeFilterField(String name) {
        super(name);
    }

    @Override
    public FieldType getType() {
        return FilterFieldType.DATE_TIME_FILTER;
    }

    @Override
    public String toString(LocalDateTimeFilter value) {
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

    public LocalDateTime getMinValue() {
        return minValue;
    }

    public void setMinValue(LocalDateTime minValue) {
        this.minValue = minValue;
    }

    public LocalDateTime getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(LocalDateTime maxValue) {
        this.maxValue = maxValue;
    }

    public ChronoUnit getLengthUnit() {
        return lengthUnit;
    }

    public void setLengthUnit(ChronoUnit lengthUnit) {
        this.lengthUnit = lengthUnit;
    }
}