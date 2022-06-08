package org.bardframework.form.filter.field;

import org.bardframework.form.common.FormField;
import org.bardframework.form.filter.FilterFieldType;
import org.bardframework.form.filter.LongFilter;

public class NumberFilterField extends FormField<LongFilter> {
    private Long minValue;
    private Long maxValue;
    private Long minLength;
    private Long maxLength;

    public NumberFilterField() {
    }

    protected NumberFilterField(String name) {
        super(name);
    }

    @Override
    public FilterFieldType getType() {
        return FilterFieldType.NUMBER_FILTER;
    }

    @Override
    public String toString(LongFilter value) {
        return String.join(SEPARATOR, null == value.getFrom() ? "" : value.getFrom().toString(), null == value.getTo() ? "" : value.getTo().toString());
    }

    public Long getMinValue() {
        return minValue;
    }

    public void setMinValue(Long minValue) {
        this.minValue = minValue;
    }

    public Long getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Long maxValue) {
        this.maxValue = maxValue;
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
}