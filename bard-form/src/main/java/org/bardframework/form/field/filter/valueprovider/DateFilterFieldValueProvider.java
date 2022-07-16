package org.bardframework.form.field.filter.valueprovider;

import org.bardframework.form.field.filter.DateFilterField;
import org.bardframework.form.field.value.FieldValueProvider;
import org.bardframework.form.model.filter.LocalDateFilter;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

public class DateFilterFieldValueProvider implements FieldValueProvider<DateFilterField, LocalDateFilter> {

    @Override
    public LocalDateFilter getValue(DateFilterField field, HttpServletRequest httpRequest) {
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
            return new LocalDateFilter().setFrom(maxValue.minusDays(length)).setTo(maxValue);
        }
        LocalDate minValue = field.getMinValue();
        if (null != minValue) {
            return new LocalDateFilter().setFrom(minValue).setTo(minValue.plusDays(length));
        }
        LocalDate now = LocalDate.now();
        return new LocalDateFilter().setFrom(now.minusDays(length)).setTo(now);
    }
}
