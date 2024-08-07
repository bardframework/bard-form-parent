package org.bardframework.form.field.filter;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.RandomUtils;
import org.bardframework.commons.utils.DateTimeUtils;
import org.bardframework.form.field.input.InputField;
import org.bardframework.form.field.input.InputFieldDataProvider;
import org.bardframework.form.model.filter.LongFilter;
import org.springframework.boot.test.context.TestComponent;

import java.time.LocalDate;

@TestComponent
public class DateFilterFieldDataProvider implements InputFieldDataProvider<DateFilterField, LongFilter> {

    @Override
    public LongFilter getValidValue(DateFilterField field) {
        boolean generateNullValue = !Boolean.TRUE.equals(field.getRequired()) && RandomUtils.nextBoolean();
        if (generateNullValue) {
            return null;
        }
        //FIXME buggy
        long minValueValid = null == field.getMinValue() ? DateTimeUtils.toEpochMills(LocalDate.of(1000, 1, 1)) : field.getMinValue();
        Long maxValueValid = null == field.getMaxValue() ? DateTimeUtils.toEpochMills(LocalDate.of(3000, 1, 1)) : field.getMaxValue();
        long minLengthValid = null == field.getMinLength() ? 0 : field.getMinLength();
        long maxLengthValid = null == field.getMaxLength() ? Long.MAX_VALUE : field.getMaxLength();
        long fromDayLength = RandomUtils.nextLong(0, 100);
        Long from = minValueValid + fromDayLength;
        Long to = minValueValid + RandomUtils.nextLong(minLengthValid + fromDayLength, minLengthValid + fromDayLength + maxLengthValid);
        return new LongFilter().setFrom(from).setTo(to);
    }

    @Override
    public LongFilter getInvalidValue(DateFilterField field) {
        if (Boolean.TRUE.equals(field.getRequired())) {
            return null;
        }
        if (null != field.getMinValue()) {
            return new LongFilter().setFrom(field.getMinValue() - RandomUtils.nextInt(1, 100)).setTo(field.getMinValue());
        }
        if (null != field.getMaxValue()) {
            return new LongFilter().setFrom(field.getMaxValue()).setTo(field.getMaxValue() + RandomUtils.nextInt(1, 100));
        }
        if (null != field.getMinLength()) {
            return new LongFilter().setFrom(DateTimeUtils.getTodayUtc()).setTo(DateTimeUtils.getTodayUtc() + RandomUtils.nextLong(0, field.getMinLength()));
        }
        if (null != field.getMaxLength()) {
            return new LongFilter().setFrom(DateTimeUtils.getTodayUtc()).setTo(DateTimeUtils.getTodayUtc() + field.getMaxLength() + RandomUtils.nextInt(1, 100));
        }
        throw new IllegalStateException("can't generate invalid value for field: " + field.getName());
    }

    @Override
    public void set(ObjectNode objectNode, String property, LongFilter value) {
        this.set(objectNode, property + "From", null == value ? null : value.getFrom());
        this.set(objectNode, property + "To", null == value ? null : value.getTo());
    }

    public void set(ObjectNode objectNode, String property, Long value) {
        if (null == value) {
            objectNode.remove(property);
        } else {
            objectNode.put(property, value.toString());
        }
    }

    @Override
    public boolean supports(InputField<?> field) {
        return DateFilterField.class.equals(field.getClass());
    }
}