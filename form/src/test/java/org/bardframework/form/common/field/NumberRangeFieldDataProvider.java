package org.bardframework.form.common.field;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.RandomUtils;
import org.bardframework.form.common.field.base.FormField;
import org.bardframework.form.common.field.common.RangeValue;
import org.springframework.boot.test.context.TestComponent;

@TestComponent
public class NumberRangeFieldDataProvider implements FieldDataProvider<NumberRangeField, RangeValue<Long>> {

    @Override
    public RangeValue<Long> getValidValue(NumberRangeField field) {
        boolean generateNullValue = !Boolean.TRUE.equals(field.getRequired()) && RandomUtils.nextBoolean();
        if (generateNullValue) {
            return null;
        }
        //FIXME buggy
        long minValueValid = null == field.getMinValue() ? 0 : field.getMinValue();
        long maxValueValid = null == field.getMaxValue() ? Long.MAX_VALUE / 3 : field.getMaxValue();
        long minLengthValid = null == field.getMinLength() ? 1 : field.getMinLength();
        long maxLengthValid = null == field.getMaxLength() ? Long.MAX_VALUE / 3 : field.getMaxLength();
        Long from = RandomUtils.nextLong(minValueValid, maxValueValid - 2);
        Long to = RandomUtils.nextLong(from + minLengthValid, from + maxLengthValid);
        return new RangeValue<>(from, to);
    }

    @Override
    public RangeValue<Long> getInvalidValue(NumberRangeField field) {
        if (Boolean.TRUE.equals(field.getRequired())) {
            return null;
        }
        long minLengthInValid = 0;
        long maxLengthInValid = 0;
        if (null != field.getMinLength()) {
            minLengthInValid = field.getMinLength() - RandomUtils.nextLong(1, field.getMinLength());
        }
        if (null != field.getMaxLength()) {
            maxLengthInValid = field.getMaxLength() + RandomUtils.nextLong(1, 100);
        }

        Long from = null;
        if (null != field.getMinValue()) {
            from = RandomUtils.nextLong(field.getMinValue() - 20, field.getMinValue() - 1);
        }
        if (null != field.getMaxValue()) {
            from = RandomUtils.nextLong(field.getMaxValue() + 1, field.getMaxValue() + 20);
        }
        if (null == from) {
            throw new IllegalStateException("can't generate invalid value for field: " + field.getName());
        }
        if (null != field.getMinLength()) {
            return new RangeValue<>(from, from + minLengthInValid);
        }
        if (null != field.getMaxLength()) {
            return new RangeValue<>(from, from + maxLengthInValid);
        }
        Long to = RandomUtils.nextLong(from + 1, from + 20);
        return new RangeValue<>(from, to);
    }

    @Override
    public void set(ObjectNode objectNode, String property, RangeValue<Long> value) {
        this.set(objectNode, property + "From", null == value ? null : value.getFrom());
        this.set(objectNode, property + "To", null == value ? null : value.getTo());
    }

    public void set(ObjectNode objectNode, String property, Long value) {
        if (null == value) {
            objectNode.remove(property);
        } else {
            objectNode.put(property, value);
        }
    }

    @Override
    public boolean supports(FormField<?> field) {
        return NumberRangeField.class.equals(field.getClass());
    }
}