package org.bardframework.form.filter.field;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.RandomUtils;
import org.bardframework.form.field.filter.DateFilterField;
import org.bardframework.form.field.input.InputField;
import org.bardframework.form.field.input.InputFieldDataProvider;
import org.bardframework.form.model.filter.LocalDateFilter;
import org.springframework.boot.test.context.TestComponent;

import java.time.LocalDate;

@TestComponent
public class DateFilterFieldDataProvider implements InputFieldDataProvider<DateFilterField, LocalDateFilter> {

    @Override
    public LocalDateFilter getValidValue(DateFilterField field) {
        boolean generateNullValue = !Boolean.TRUE.equals(field.getRequired()) && RandomUtils.nextBoolean();
        if (generateNullValue) {
            return null;
        }
        //FIXME buggy
        LocalDate minValueValid = null == field.getMinValue() ? LocalDate.of(1000, 1, 1) : field.getMinValue();
        LocalDate maxValueValid = null == field.getMaxValue() ? LocalDate.of(3000, 1, 1) : field.getMaxValue();
        long minLengthValid = null == field.getMinLength() ? 0 : field.getMinLength();
        long maxLengthValid = null == field.getMaxLength() ? Long.MAX_VALUE : field.getMaxLength();
        long fromDayLength = RandomUtils.nextLong(0, 100);
        LocalDate from = minValueValid.plusDays(fromDayLength);
        LocalDate to = minValueValid.plusDays(RandomUtils.nextLong(minLengthValid + fromDayLength, minLengthValid + fromDayLength + maxLengthValid));
        return new LocalDateFilter().setFrom(from).setTo(to);
    }

    @Override
    public LocalDateFilter getInvalidValue(DateFilterField field) {
        if (Boolean.TRUE.equals(field.getRequired())) {
            return null;
        }
        if (null != field.getMinValue()) {
            return new LocalDateFilter().setFrom(field.getMinValue().minusDays(RandomUtils.nextInt(1, 100))).setTo(field.getMinValue());
        }
        if (null != field.getMaxValue()) {
            return new LocalDateFilter().setFrom(field.getMaxValue()).setTo(field.getMaxValue().plusDays(RandomUtils.nextInt(1, 100)));
        }
        if (null != field.getMinLength()) {
            return new LocalDateFilter().setFrom(LocalDate.now()).setTo(LocalDate.now().plusDays(RandomUtils.nextLong(0, field.getMinLength())));
        }
        if (null != field.getMaxLength()) {
            return new LocalDateFilter().setFrom(LocalDate.now()).setTo(LocalDate.now().plusDays(field.getMaxLength() + RandomUtils.nextInt(1, 100)));
        }
        throw new IllegalStateException("can't generate invalid value for field: " + field.getName());
    }

    @Override
    public void set(ObjectNode objectNode, String property, LocalDateFilter value) {
        this.set(objectNode, property + "From", null == value ? null : value.getFrom());
        this.set(objectNode, property + "To", null == value ? null : value.getTo());
    }

    public void set(ObjectNode objectNode, String property, LocalDate value) {
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