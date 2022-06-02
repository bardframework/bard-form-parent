package org.bardframework.form.field;

import org.apache.commons.collections4.CollectionUtils;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;
import org.bardframework.form.common.field.ListField;
import org.bardframework.form.common.field.base.FormField;
import org.bardframework.form.field.base.FormFieldTemplate;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ListFieldTemplate extends FormFieldTemplate<ListField, List<String>> {

    protected ListFieldTemplate(String name) {
        super(name);
    }

    @Override
    public boolean isValid(ListField field, List<String> values) {
        if (CollectionUtils.isEmpty(values) && Boolean.TRUE.equals(field.getDisable())) {
            LOGGER.debug("filed [{}] is required, but it's value is empty", field.getName());
            return false;
        }
        if (values.size() > field.getMaxCount()) {
            LOGGER.debug("data count[{}] of filed[{}] is greater than maximum[{}]", values.size(), field.getName(), field.getMaxCount());
            return false;
        }
        if (null != field.getMinLength() && values.stream().anyMatch(value -> value.length() < field.getMinLength())) {
            LOGGER.debug("filed [{}] min length is [{}], but one of it's values length is smaller than minimum", field.getName(), field.getMinLength());
            return false;
        }
        if (null != field.getMaxLength() && values.stream().anyMatch(value -> value.length() > field.getMaxLength())) {
            LOGGER.debug("filed [{}] max length is [{}], but one of it's values length is greater than maximum", field.getName(), field.getMaxLength());
            return false;
        }
        return true;
    }

    @Override
    public void fill(FormTemplate formTemplate, ListField field, Map<String, String> args, Locale locale) throws Exception {
        super.fill(formTemplate, field, args, locale);
        field.setMinLength(FormUtils.getIntegerValue(formTemplate, this.getName(), "minLength", locale, args));
        field.setMaxLength(FormUtils.getIntegerValue(formTemplate, this.getName(), "maxLength", locale, args));
        field.setMaxCount(FormUtils.getIntegerValue(formTemplate, this.getName(), "maxCount", locale, args));
    }

    @Override
    public List<String> toValue(String value) {
        return List.of(value.split(FormField.SEPARATOR));
    }
}