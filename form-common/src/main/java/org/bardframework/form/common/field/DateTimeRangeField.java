package org.bardframework.form.common.field;

import com.fasterxml.jackson.annotation.JsonTypeName;
import org.bardframework.form.common.FieldType;
import org.bardframework.form.common.FieldTypeBase;
import org.bardframework.form.common.field.base.FormField;
import org.bardframework.form.common.field.common.RangeValue;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@JsonTypeName("DATE_TIME_RANGE")
public class DateTimeRangeField extends FormField<RangeValue<LocalDateTime>> {
    private Long minLength;
    private Long maxLength;
    private ChronoUnit lengthUnit;
    private LocalDateTime minValue;
    private LocalDateTime maxValue;

    public DateTimeRangeField() {
    }

    public DateTimeRangeField(String name) {
        super(name);
    }

    @Override
    public FieldType<?> getType() {
        return FieldTypeBase.DATE_TIME_RANGE;
    }

    @Override
    public String toString(RangeValue<LocalDateTime> value) {
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

    public ChronoUnit getLengthUnit() {
        return lengthUnit;
    }

    public void setLengthUnit(ChronoUnit lengthUnit) {
        this.lengthUnit = lengthUnit;
    }
}