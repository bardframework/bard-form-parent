package org.bardframework.form.field.input;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.bardframework.form.field.InputField;
import org.springframework.boot.test.context.TestComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@TestComponent
public class ListFieldDataProvider implements InputFieldDataProvider<ListField, List<String>> {

    @Override
    public List<String> getValidValue(ListField field) {
        boolean generateNullValue = !Boolean.TRUE.equals(field.getRequired()) && RandomUtils.nextBoolean();
        if (generateNullValue) {
            return null;
        }
        return Collections.singletonList(RandomStringUtils.randomAlphanumeric(5, 10));
    }

    @Override
    public List<String> getInvalidValue(ListField field) {
        //TODO check regex when generate valid and invalid value
        //TODO all scenarios with random case
        if (null != field.getMinLength()) {
            List<String> returnedValue = new ArrayList<>();
            for (int i = 0; i < field.getMaxCount(); i++) {
                returnedValue.add(RandomStringUtils.randomAlphanumeric(0, field.getMinLength()));
            }
            return returnedValue;
        }
        if (null != field.getMaxLength()) {
            List<String> returnedValue = new ArrayList<>();
            for (int i = 0; i < field.getMaxCount(); i++) {
                returnedValue.add(RandomStringUtils.randomAlphanumeric(field.getMaxLength() + 1, RandomUtils.nextInt(field.getMaxLength() + 1, field.getMaxLength() * 2)));
            }
            return returnedValue;
        }
        if (null != field.getMaxCount()) {
            List<String> returnedValue = new ArrayList<>();
            for (int i = 0; i < field.getMaxCount() + 1; i++) {
                returnedValue.add(RandomStringUtils.randomAlphanumeric(5, 10));
            }
            return returnedValue;
        }
        if (Boolean.TRUE.equals(field.getRequired())) {
            return null;
        }
        throw new IllegalStateException("can't generate invalid value for field: " + field.getName());
    }

    @Override
    public void set(ObjectNode objectNode, String property, List<String> value) {
        if (null == value) {
            objectNode.remove(property);
        } else {
            objectNode.putArray(property).addAll(value.stream().map(TextNode::new).collect(Collectors.toList()));
        }
    }

    @Override
    public boolean supports(InputField<?> field) {
        return ListField.class.equals(field.getClass());
    }
}