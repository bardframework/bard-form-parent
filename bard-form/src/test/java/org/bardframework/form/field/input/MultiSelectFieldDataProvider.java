package org.bardframework.form.field.input;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.TestComponent;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@TestComponent
public class MultiSelectFieldDataProvider implements InputFieldDataProvider<MultiSelectField, List<String>> {
    private static final Logger log = LoggerFactory.getLogger(MultiSelectFieldDataProvider.class);

    @Override
    public List<String> getValidValue(MultiSelectField field) throws Exception {
        if (CollectionUtils.isEmpty(field.getOptions())) {
            if (Boolean.TRUE.equals(field.getRequired())) {
                throw new IllegalStateException("field is required, but options is empty: " + field.getName());
            }
            return null;
        }
        boolean generateNullValue = !Boolean.TRUE.equals(field.getRequired()) && RandomUtils.nextBoolean();
        if (generateNullValue) {
            return null;
        }
        return Collections.singletonList(field.getOptions().get(RandomUtils.nextInt(0, field.getOptions().size())).getId());
    }

    @Override
    public List<String> getInvalidValue(MultiSelectField field) throws Exception {
        if (Boolean.TRUE.equals(field.getRequired())) {
            return null;
        }
        return List.of(RandomStringUtils.randomAlphanumeric(6));
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
        return field instanceof MultiSelectField;
    }
}