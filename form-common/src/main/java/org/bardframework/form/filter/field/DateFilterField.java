package org.bardframework.form.filter.field;

import org.bardframework.form.common.FormField;
import org.bardframework.form.filter.FilterFieldType;
import org.bardframework.form.filter.LocalDateFilter;

import java.time.LocalDate;

public class DateFilterField extends FormField<LocalDateFilter> {
    private Long minLength;
    private Long maxLength;
    private LocalDate minValue;
    private LocalDate maxValue;

    public DateFilterField() {
    }

    public DateFilterField(String name) {
        super(name);
    }

    @Override
    public FilterFieldType getType() {
        return FilterFieldType.DATE_FILTER;
    }

    @Override
    public String toString(LocalDateFilter value) {
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

    public LocalDate getMinValue() {
        return minValue;
    }

    public void setMinValue(LocalDate minValue) {
        this.minValue = minValue;
    }

    public LocalDate getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(LocalDate maxValue) {
        this.maxValue = maxValue;
    }
}