package org.bardframework.form.field.value;

import org.bardframework.form.common.field.DateRangeField;
import org.bardframework.form.common.field.common.RangeValue;

import java.time.LocalDate;

public class DateRangeValueProvider implements FieldValueProvider<DateRangeField, RangeValue<LocalDate>> {

    @Override
    public RangeValue<LocalDate> getValue(DateRangeField field) {
        Long length = field.getMaxLength();
        if (null == length) {
            length = field.getMinLength();
        }
        if (null == length) {
            length = 8L;
        }
        length = length - 1;
        LocalDate maxValue = field.getMaxValue();
        if (null != maxValue) {
            return new RangeValue<>(maxValue.minusDays(length), maxValue);
        }
        LocalDate minValue = field.getMinValue();
        if (null != minValue) {
            return new RangeValue<>(minValue, minValue.plusDays(length));
        }
        LocalDate now = LocalDate.now();
        return new RangeValue<>(now.minusDays(length), now);
    }
}
