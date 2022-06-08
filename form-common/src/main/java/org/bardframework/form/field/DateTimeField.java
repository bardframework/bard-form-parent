package org.bardframework.form.field;

import org.bardframework.form.common.FormField;

import java.time.LocalDateTime;

public class DateTimeField extends FormField<LocalDateTime> {
    private LocalDateTime minValue;
    private LocalDateTime maxValue;

    public DateTimeField() {
    }

    public DateTimeField(String name) {
        super(name);
    }

    @Override
    public FieldType getType() {
        return FieldType.DATE_TIME;
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

    @Override
    public String toString(LocalDateTime value) {
        return null == value ? null : value.toString();
    }
}