package org.bardframework.form;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.bardframework.form.field.Field;
import org.bardframework.form.field.input.InputField;
import org.bardframework.form.field.input.InputFieldDataProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@TestComponent
public class FormDataProvider {

    @Autowired
    private List<InputFieldDataProvider<? extends InputField<?>, ?>> fieldDataProviders;
    @Autowired
    private ObjectMapper objectMapper;

    public Map<String, Object> getValidData(BardForm form) throws Exception {
        Map<String, Object> map = new HashMap<>();
        for (Field field : form.getFields()) {
            if (!(field instanceof InputField<?> inputField)) {
                continue;
            }
            InputFieldDataProvider dataProvider = this.getDataProvider(inputField);
            map.put(inputField.getName(), dataProvider.getValidValue(inputField));
        }
        return map;
    }

    public Map<String, Object> getInvalidData(BardForm form) throws Exception {
        Map<String, Object> map = new HashMap<>();
        boolean invalidate = false;
        for (Field field : form.getFields()) {
            if (!(field instanceof InputField<?> inputField)) {
                continue;
            }
            InputFieldDataProvider dataProvider = this.getDataProvider(inputField);
            if (RandomUtils.nextBoolean()) {
                invalidate = true;
                map.put(inputField.getName(), dataProvider.getInvalidValue(inputField));
            } else {
                map.put(inputField.getName(), dataProvider.getValidValue(inputField));
            }
        }
        if (!invalidate) {
            //FIXME
        }
        return map;
    }

    public String getWithExtraProperty(List<? extends InputField<?>> fields) throws Exception {
        ObjectNode objectNode = this.getValidObjectNode(fields);
        objectNode.put(RandomStringUtils.randomAlphabetic(5, 10), RandomStringUtils.randomAlphabetic(5, 10));
        return objectMapper.writeValueAsString(objectNode);
    }

    public String getValid(List<? extends InputField<?>> fields) throws Exception {
        return objectMapper.writeValueAsString(this.getValidObjectNode(fields));
    }

    public <T> T getValid(List<? extends InputField<?>> fields, Class<T> clazz) throws Exception {
        String validJson = this.getValid(fields);
        return objectMapper.readValue(validJson, clazz);
    }

    public String getInvalid(List<? extends InputField<?>> fields) throws Exception {
        ObjectNode objectNode = this.getValidObjectNode(fields);
        this.add(objectNode, fields.get(RandomUtils.nextInt(0, fields.size())), false);
        return objectMapper.writeValueAsString(objectNode);
    }

    public ObjectNode getValidObjectNode(List<? extends InputField<?>> fields) throws Exception {
        ObjectNode node = objectMapper.createObjectNode();
        for (InputField<?> field : fields) {
            this.add(node, field, true);
        }
        return node;
    }

    private InputFieldDataProvider<?, ?> getDataProvider(InputField<?> inputField) {
        for (InputFieldDataProvider<?, ?> dataProvider : fieldDataProviders) {
            if (dataProvider.supports(inputField)) {
                return dataProvider;
            }
        }
        throw new IllegalStateException("data provider not found that supports " + inputField.getClass().getSimpleName());
    }

    private void add(ObjectNode criteria, InputField<?> field, boolean valid) throws Exception {
        for (InputFieldDataProvider fieldDataProvider : fieldDataProviders) {
            if (fieldDataProvider.supports(field)) {
                if (valid) {
                    fieldDataProvider.setValidValue(criteria, field);
                } else {
                    fieldDataProvider.setInvalidValue(criteria, field);
                }
                return;
            }
        }
        throw new IllegalStateException("no field data provider found for type " + field.getClass().getSimpleName());
    }
}
