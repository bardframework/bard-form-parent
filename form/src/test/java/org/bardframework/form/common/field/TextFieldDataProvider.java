package org.bardframework.form.common.field;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.bardframework.form.common.FormField;
import org.bardframework.form.field.TextField;
import org.springframework.boot.test.context.TestComponent;

@TestComponent
public class TextFieldDataProvider implements FieldDataProvider<TextField, String> {

    @Override
    public String getValidValue(TextField field) {
        boolean generateNullValue = !Boolean.TRUE.equals(field.getRequired()) && RandomUtils.nextBoolean();
        if (generateNullValue) {
            return null;
        }
        int minLengthValid = null == field.getMinLength() ? 1 : field.getMinLength();
        int maxLengthValid = null == field.getMaxLength() ? 1000 : field.getMaxLength();
        return RandomStringUtils.randomAlphanumeric(minLengthValid, maxLengthValid);
    }

    @Override
    public String getInvalidValue(TextField field) {
        if (null != field.getMinLength() && field.getMinLength() > 0) {
            return RandomStringUtils.randomAlphanumeric(0, field.getMinLength() - 1);
        }
        if (null != field.getMaxLength()) {
            return RandomStringUtils.randomAlphanumeric(field.getMaxLength(), field.getMaxLength() + 100);
        }
        //TODO check regex when generate valid and invalid value
        throw new IllegalStateException("can't generate invalid value for field: " + field.getName());
    }

    @Override
    public void set(ObjectNode objectNode, String property, String value) {
        if (null == value) {
            objectNode.remove(property);
        } else {
            objectNode.put(property, value);
        }
    }

    @Override
    public boolean supports(FormField<?> field) {
        return field instanceof TextField;
    }
}