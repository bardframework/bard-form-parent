package org.bardframework.form.field.input;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.boot.test.context.TestComponent;

@TestComponent
public class SwitchButtonFieldDataProvider implements InputFieldDataProvider<SwitchButtonField, Boolean> {

    @Override
    public Boolean getValidValue(SwitchButtonField field) {
        boolean generateNullValue = !Boolean.TRUE.equals(field.getRequired()) && RandomUtils.nextBoolean();
        if (generateNullValue) {
            return null;
        }
        return Boolean.TRUE.equals(field.getRequired()) || RandomUtils.nextBoolean();
    }

    @Override
    public Boolean getInvalidValue(SwitchButtonField field) {
        throw new IllegalStateException("can't generate invalid value for field: " + field.getName());
    }

    @Override
    public void set(ObjectNode objectNode, String property, Boolean value) {
        if (null == value) {
            objectNode.remove(property);
        } else {
            objectNode.put(property, value);
        }
    }

    @Override
    public boolean supports(InputField<?> field) {
        return field instanceof SwitchButtonField;
    }
}