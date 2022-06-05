package org.bardframework.form.common.field;

import com.fasterxml.jackson.annotation.JsonTypeName;
import org.bardframework.form.common.FieldType;
import org.bardframework.form.common.FieldTypeBase;
import org.bardframework.form.common.field.base.FormField;

@JsonTypeName("NUMBER")
public class NumberField extends FormField<Long> {
    private Long minValue;
    private Long maxValue;
    private String mask;

    public NumberField() {
    }

    public NumberField(String name) {
        super(name);
    }

    @Override
    public FieldType<?> getType() {
        return FieldTypeBase.NUMBER;
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