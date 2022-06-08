package org.bardframework.form.field.input;

import org.bardframework.form.field.InputField;

import java.time.LocalDate;

public class DateField extends InputField<LocalDate> {
    private LocalDate minValue;
    private LocalDate maxValue;

    public DateField() {
    }

    public DateField(String name) {
        super(name);
    }

    @Override
    public InputFieldType getType() {
        return InputFieldType.DATE;
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

    @Override
    public String toString(LocalDate value) {
        return null == value ? null : value.toString();
    }
}