package org.bardframework.form.field.input;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.bardframework.form.model.SelectOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.TestComponent;

import java.util.Set;
import java.util.stream.Collectors;

@TestComponent
public class SingleSelectFieldDataProvider implements InputFieldDataProvider<SingleSelectField, String> {
    private static final Logger log = LoggerFactory.getLogger(SingleSelectFieldDataProvider.class);

    @Override
    public String getValidValue(SingleSelectField field) throws Exception {
        Set<String> options = field.getOptions().stream().map(SelectOption::getId).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(options)) {
            if (Boolean.TRUE.equals(field.getRequired())) {
                throw new IllegalStateException("field is required, but options is empty: " + field.getName());
            } else {
                return null;
            }
        }
        boolean generateNullValue = !Boolean.TRUE.equals(field.getRequired()) && RandomUtils.nextBoolean();
        if (generateNullValue) {
            return null;
        }
        return field.getOptions().get(RandomUtils.nextInt(0, field.getOptions().size())).getId();
    }

    @Override
    public String getInvalidValue(SingleSelectField field) throws Exception {
        if (Boolean.TRUE.equals(field.getRequired())) {
            return null;
        }
        String random = RandomStringUtils.randomAlphanumeric(6);
        if (CollectionUtils.isEmpty(field.getOptions())) {
            return random;
        }
        boolean findInvalidData = false;
        for (int i = 0; i < 10; i++) {
            String finalRandom = random;
            if (field.getOptions().stream().anyMatch(selectOptionModel -> selectOptionModel.getId().equals(finalRandom))) {
                random = RandomStringUtils.randomAlphanumeric(6);
            } else {
                findInvalidData = true;
                break;
            }
        }
        if (!findInvalidData) {
            throw new IllegalStateException("can't generate invalid value for field: " + field.getName());
        }
        return random;
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
    public boolean supports(InputField<?> field) {
        return field instanceof SingleSelectField;
    }
}