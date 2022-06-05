package org.bardframework.form.common.field;

import com.fasterxml.jackson.annotation.JsonTypeName;
import org.bardframework.form.common.FieldType;
import org.bardframework.form.common.FieldTypeBase;
import org.bardframework.form.common.field.base.FormField;
import org.bardframework.form.common.field.common.RangeValue;

import java.time.LocalDate;

@JsonTypeName("DATE_RANGE")
public class DateRangeField extends FormField<RangeValue<LocalDate>> {
    private Long minLength;
    private Long maxLength;
    private LocalDate minValue;
    private LocalDate maxValue;

    public DateRangeField() {
    }

    public DateRangeField(String name) {
        super(name);
    }

    @Override
    public FieldType<?> getType() {
        return FieldTypeBase.DATE_RANGE;
    }

    @Override
    public String toString(RangeValue<LocalDate> value) {
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