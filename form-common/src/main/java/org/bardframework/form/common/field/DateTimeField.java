package org.bardframework.form.common.field;

import org.bardframework.form.common.FieldType;
import org.bardframework.form.common.FieldTypeBase;
import org.bardframework.form.common.field.base.FormField;

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
    public FieldType<?> getType() {
        return FieldTypeBase.DATE_TIME;
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