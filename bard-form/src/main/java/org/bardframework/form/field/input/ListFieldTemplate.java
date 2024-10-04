package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@Getter
@Setter
public class ListFieldTemplate extends InputFieldTemplateAbstract<ListField, List<String>> {

    public ListFieldTemplate(String name) {
        super(name);
    }

    @Override
    public boolean isValid(String flowToken, ListField field, List<String> values, Map<String, String> flowData) {
        if (CollectionUtils.isEmpty(values)) {
            if (Boolean.TRUE.equals(field.getRequired())) {
                log.debug("field [{}] is required, but it's value is empty", field.getName());
                return false;
            }
            return true;
        }
        if (values.size() > field.getMaxCount()) {
            log.debug("data count[{}] of field[{}] is greater than maximum[{}]", values.size(), field.getName(), field.getMaxCount());
            return false;
        }
        if (null != field.getMinLength() && values.stream().anyMatch(value -> value.length() < field.getMinLength())) {
            log.debug("field [{}] min length is [{}], but one of it's values length is smaller than minimum", field.getName(), field.getMinLength());
            return false;
        }
        if (null != field.getMaxLength() && values.stream().anyMatch(value -> value.length() > field.getMaxLength())) {
            log.debug("field [{}] max length is [{}], but one of it's values length is greater than maximum", field.getName(), field.getMaxLength());
            return false;
        }
        return true;
    }

    @Override
    public void fill(FormTemplate formTemplate, ListField field, Map<String, String> args, Locale locale) throws Exception {
        super.fill(formTemplate, field, args, locale);
        field.setMinLength(FormUtils.getFieldIntegerProperty(formTemplate, this, "minLength", locale, args, this.getDefaultValue().getMinLength()));
        field.setMaxLength(FormUtils.getFieldIntegerProperty(formTemplate, this, "maxLength", locale, args, this.getDefaultValue().getMaxLength()));
        field.setMaxCount(FormUtils.getFieldIntegerProperty(formTemplate, this, "maxCount", locale, args, this.getDefaultValue().getMaxCount()));
        field.setBulkAdd(FormUtils.getFieldBooleanProperty(formTemplate, this, "bulkAdd", locale, args, this.getDefaultValue().getBulkAdd()));
        field.setSubmitType(this.getDefaultValue().getSubmitType());
    }

    @Override
    public List<String> toValue(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return List.of(value.split(InputField.SEPARATOR));
    }
}