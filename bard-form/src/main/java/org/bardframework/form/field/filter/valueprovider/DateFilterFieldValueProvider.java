package org.bardframework.form.field.filter.valueprovider;

import org.bardframework.commons.utils.DateTimeUtils;
import org.bardframework.form.field.filter.DateFilterField;
import org.bardframework.form.field.value.FieldValueProvider;
import org.bardframework.form.model.filter.LongFilter;

public class DateFilterFieldValueProvider implements FieldValueProvider<DateFilterField, LongFilter> {

    @Override
    public LongFilter getValue(DateFilterField field) {
        Long length = field.getMaxLength();
        if (null == length) {
            length = field.getMinLength();
        }
        if (null == length) {
            length = 8L;
        }
        length = length - 1;
        Long maxValue = field.getMaxValue();
        if (null != maxValue) {
            return new LongFilter().setFrom(maxValue - length).setTo(maxValue);
        }
        Long minValue = field.getMinValue();
        if (null != minValue) {
            return new LongFilter().setFrom(minValue).setTo(minValue + length);
        }
        Long now = DateTimeUtils.getTodayUtc();
        return new LongFilter().setFrom(now - length).setTo(now);
    }
}
