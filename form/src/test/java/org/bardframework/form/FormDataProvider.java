package org.bardframework.form;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.bardframework.form.common.Form;
import org.bardframework.form.common.field.FieldDataProvider;
import org.bardframework.form.common.field.base.Field;
import org.bardframework.form.common.field.base.FormField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@TestComponent
public class FormDataProvider {

    @Autowired
    private List<FieldDataProvider<? extends FormField<?>, ?>> fieldDataProviders;
    @Autowired
    private ObjectMapper objectMapper;

    public Map<String, String> getValidData(Form form) throws Exception {
        Map<String, String> map = new HashMap<>();
        for (Field field : form.getFields()) {
            if (!(field instanceof FormField<?>)) {
                continue;
            }
            FormField<?> formField = (FormField<?>) field;
            FieldDataProvider dataProvider = this.getDataProvider(formField);
            map.put(formField.getName(), dataProvider.getValidValueString(formField));
        }
        return map;
    }

    public Map<String, String> getInvalidData(Form form) throws Exception {
        Map<String, String> map = new HashMap<>();
        boolean invalidate = false;
        for (Field field : form.getFields()) {
            if (!(field instanceof FormField<?>)) {
                continue;
            }
            FormField<?> formField = (FormField<?>) field;
            FieldDataProvider dataProvider = this.getDataProvider(formField);
            if (RandomUtils.nextBoolean()) {
                invalidate = true;
                map.put(formField.getName(), dataProvider.getInvalidValueString(formField));
            } else {
                map.put(formField.getName(), dataProvider.getValidValueString(formField));
            }
        }
        if (!invalidate) {
            //FIXME
        }
        return map;
    }

    public String getWithExtraProperty(List<? extends FormField<?>> fields) throws Exception {
        ObjectNode objectNode = this.getValidObjectNode(fields);
        objectNode.put(RandomStringUtils.randomAlphabetic(5, 10), RandomStringUtils.randomAlphabetic(5, 10));
        return objectMapper.writeValueAsString(objectNode);
    }

    public String getValid(List<? extends FormField<?>> fields) throws Exception {
        return objectMapper.writeValueAsString(this.getValidObjectNode(fields));
    }

    public <T> T getValid(List<? extends FormField<?>> fields, Class<T> clazz) throws Exception {
        String validJson = this.getValid(fields);
        return objectMapper.readValue(validJson, clazz);
    }

    public String getInvalid(List<? extends FormField<?>> fields) throws Exception {
        ObjectNode objectNode = this.getValidObjectNode(fields);
        this.add(objectNode, fields.get(RandomUtils.nextInt(0, fields.size())), false);
        return objectMapper.writeValueAsString(objectNode);
    }

    public ObjectNode getValidObjectNode(List<? extends FormField<?>> fields) throws Exception {
        ObjectNode node = objectMapper.createObjectNode();
        for (FormField<?> field : fields) {
            this.add(node, field, true);
        }
        return node;
    }

    private FieldDataProvider getDataProvider(FormField<?> formField) {
        for (FieldDataProvider<?, ?> dataProvider : fieldDataProviders) {
            if (dataProvider.supports(formField)) {
                return dataProvider;
            }
        }
        throw new IllegalStateException("data provider not found that supports " + formField.getClass().getSimpleName());
    }

    private void add(ObjectNode criteria, FormField<?> field, boolean valid) throws Exception {
        for (FieldDataProvider fieldDataProvider : fieldDataProviders) {
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
