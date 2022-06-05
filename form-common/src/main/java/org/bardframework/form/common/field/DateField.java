package org.bardframework.form.common.field;

import com.fasterxml.jackson.annotation.JsonTypeName;
import org.bardframework.form.common.FieldType;
import org.bardframework.form.common.FieldTypeBase;
import org.bardframework.form.common.field.base.FormField;

import java.time.LocalDate;

@JsonTypeName("DATE")
public class DateField extends FormField<LocalDate> {
    private LocalDate minValue;
    private LocalDate maxValue;

    public DateField() {
    }

    public DateField(String name) {
        super(name);
    }

    @Override
    public FieldType<?> getType() {
        return FieldTypeBase.DATE;
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