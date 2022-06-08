package org.bardframework.form.filter.field;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.RandomUtils;
import org.bardframework.form.common.FormField;
import org.bardframework.form.common.field.FieldDataProvider;
import org.bardframework.form.filter.LongFilter;
import org.springframework.boot.test.context.TestComponent;

@TestComponent
public class NumberFilterFieldDataProvider implements FieldDataProvider<NumberFilterField, LongFilter> {

    @Override
    public LongFilter getValidValue(NumberFilterField field) {
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
        return new LongFilter().setFrom(from).setTo(to);
    }

    @Override
    public LongFilter getInvalidValue(NumberFilterField field) {
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
            return new LongFilter().setFrom(from).setTo(from + minLengthInValid);
        }
        if (null != field.getMaxLength()) {
            return new LongFilter().setFrom(from).setTo(from + maxLengthInValid);
        }
        Long to = RandomUtils.nextLong(from + 1, from + 20);
        return new LongFilter().setFrom(from).setTo(to);
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
            objectNode.put(property, value);
        }
    }

    @Override
    public boolean supports(FormField<?> field) {
        return NumberFilterField.class.equals(field.getClass());
    }
}