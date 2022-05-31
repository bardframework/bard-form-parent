package org.bardframework.form.common.field;

import org.bardframework.form.common.FieldType;
import org.bardframework.form.common.FieldTypeBase;
import org.bardframework.form.common.field.base.FormField;
import org.bardframework.form.common.field.common.RangeValue;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class TimeRangeField extends FormField<RangeValue<LocalTime>> {
    private Long minLength;
    private Long maxLength;
    private ChronoUnit lengthUnit;
    private LocalTime minValue;
    private LocalTime maxValue;

    public TimeRangeField() {
    }

    public TimeRangeField(String name) {
        super(name);
    }

    @Override
    public FieldType<?> getType() {
        return FieldTypeBase.TIME_RANGE;
    }

    @Override
    public String toString(RangeValue<LocalTime> value) {
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

    public ChronoUnit getLengthUnit() {
        return lengthUnit;
    }

    public void setLengthUnit(ChronoUnit lengthUnit) {
        this.lengthUnit = lengthUnit;
    }
}