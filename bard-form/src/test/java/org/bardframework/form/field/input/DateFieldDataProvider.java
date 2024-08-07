package org.bardframework.form.field.input;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.RandomUtils;
import org.bardframework.commons.utils.DateTimeUtils;
import org.springframework.stereotype.Component;

@Component
public class DateFieldDataProvider implements InputFieldDataProvider<DateField, Long> {

    @Override
    public Long getValidValue(DateField field) {
        boolean generateNullValue = !Boolean.TRUE.equals(field.getRequired()) && RandomUtils.nextBoolean();
        if (generateNullValue) {
            return null;
        }
        return DateTimeUtils.getTodayUtc() - (RandomUtils.nextInt(0, 10_000) * DateTimeUtils.ONE_DAY_MILLS);
    }

    @Override
    public Long getInvalidValue(DateField field) {
        if (null != field.getMinValue()) {
            return field.getMinValue() - (RandomUtils.nextInt(0, 20) * DateTimeUtils.ONE_DAY_MILLS);
        }
        if (null != field.getMaxValue()) {
            return field.getMaxValue() + (RandomUtils.nextInt(0, 20) * DateTimeUtils.ONE_DAY_MILLS);
        }
        if (Boolean.TRUE.equals(field.getRequired())) {
            return null;
        }
        //TODO check regex when generate valid and invalid value
        throw new IllegalStateException("can't generate invalid value for field: " + field.getName());
    }

    @Override
    public void set(ObjectNode objectNode, String property, Long value) {
        if (null == value) {
            objectNode.remove(property);
        } else {
            objectNode.put(property, value.toString());
        }
    }

    @Override
    public boolean supports(InputField<?> field) {
        return DateField.class.equals(field.getClass());
    }
}