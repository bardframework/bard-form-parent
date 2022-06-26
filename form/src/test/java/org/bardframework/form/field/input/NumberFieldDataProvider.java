package org.bardframework.form.field.input;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.boot.test.context.TestComponent;

@TestComponent
public class NumberFieldDataProvider implements InputFieldDataProvider<NumberField, Long> {

    @Override
    public Long getValidValue(NumberField field) {
        boolean generateNullValue = !Boolean.TRUE.equals(field.getRequired()) && RandomUtils.nextBoolean();
        if (generateNullValue) {
            return null;
        }
        long minValueValid = null == field.getMinValue() ? 0 : field.getMinValue();
        long maxValueValid = null == field.getMaxValue() ? Long.MAX_VALUE : field.getMaxValue();
        return RandomUtils.nextLong(minValueValid, maxValueValid);
    }

    @Override
    public Long getInvalidValue(NumberField field) {
        if (null != field.getMaxValue()) {
            return RandomUtils.nextLong(field.getMaxValue() + 1, field.getMaxValue() + 20);
        }
        if (null != field.getMinValue()) {
            return RandomUtils.nextLong(field.getMinValue() - 20, field.getMinValue() - 1);
        }
        if (Boolean.TRUE.equals(field.getRequired())) {
            return null;
        }
        throw new IllegalStateException("can't generate invalid value for field: " + field.getName());
    }

    @Override
    public void set(ObjectNode objectNode, String property, Long value) {
        if (null == value) {
            objectNode.remove(property);
        } else {
            objectNode.put(property, value);
        }
    }

    @Override
    public boolean supports(InputField<?> field) {
        return NumberField.class.equals(field.getClass());
    }
}