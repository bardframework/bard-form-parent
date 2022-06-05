package org.bardframework.form.common.field;

import org.bardframework.form.common.FieldType;
import org.bardframework.form.common.FieldTypeBase;
import org.bardframework.form.common.field.base.FormField;

import java.time.LocalTime;

public class TimeField extends FormField<LocalTime> {
    private LocalTime minValue;
    private LocalTime maxValue;

    public TimeField() {
    }

    public TimeField(String name) {
        super(name);
    }

    @Override
    public FieldType<?> getType() {
        return FieldTypeBase.TIME;
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

    @Override
    public String toString(LocalTime value) {
        return null == value ? null : value.toString();
    }
}