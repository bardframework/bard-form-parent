package org.bardframework.form.field.input;

import org.bardframework.form.field.InputField;

import java.time.LocalDateTime;

public class DateTimeField extends InputField<LocalDateTime> {
    private LocalDateTime minValue;
    private LocalDateTime maxValue;

    public DateTimeField() {
    }

    public DateTimeField(String name) {
        super(name);
    }

    @Override
    public InputFieldType getType() {
        return InputFieldType.DATE_TIME;
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