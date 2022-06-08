package org.bardframework.form.field.input;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.RandomUtils;
import org.bardframework.form.field.InputField;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DateFieldDataProvider implements InputFieldDataProvider<DateField, LocalDate> {

    @Override
    public LocalDate getValidValue(DateField field) {
        boolean generateNullValue = !Boolean.TRUE.equals(field.getRequired()) && RandomUtils.nextBoolean();
        if (generateNullValue) {
            return null;
        }
        return LocalDate.now().minusDays(RandomUtils.nextInt(0, 10_000));
    }

    @Override
    public LocalDate getInvalidValue(DateField field) {
        if (null != field.getMinValue()) {
            return field.getMinValue().minusDays(RandomUtils.nextInt(0, 20));
        }
        if (null != field.getMaxValue()) {
            return field.getMaxValue().plusDays(RandomUtils.nextInt(0, 20));
        }
        if (Boolean.TRUE.equals(field.getRequired())) {
            return null;
        }
        //TODO check regex when generate valid and invalid value
        throw new IllegalStateException("can't generate invalid value for field: " + field.getName());
    }

    @Override
    public void set(ObjectNode objectNode, String property, LocalDate value) {
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