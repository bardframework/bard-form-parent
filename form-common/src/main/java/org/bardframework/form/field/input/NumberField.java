package org.bardframework.form.field.input;

import org.bardframework.form.field.InputField;

public class NumberField extends InputField<Long> {
    private Long minValue;
    private Long maxValue;
    private String mask;

    public NumberField() {
    }

    public NumberField(String name) {
        super(name);
    }

    @Override
    public InputFieldType getType() {
        return InputFieldType.NUMBER;
    }

    @Override
    public String toString(Long value) {
        return null == value ? null : value.toString();
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

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

}