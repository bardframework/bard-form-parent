package org.bardframework.form.field;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;
import org.bardframework.form.common.field.MultiSelectField;
import org.bardframework.form.common.field.base.FormField;
import org.bardframework.form.field.base.FormFieldTemplate;
import org.bardframework.form.field.option.OptionDataSource;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class MultiSelectFieldTemplate extends FormFieldTemplate<MultiSelectField, List<String>> {

    protected final OptionDataSource optionDataSource;

    protected MultiSelectFieldTemplate(String name, OptionDataSource optionDataSource) {
        super(name);
        this.optionDataSource = optionDataSource;
    }

    @Override
    public void fill(FormTemplate formTemplate, MultiSelectField field, Map<String, String> args, Locale locale) throws Exception {
        super.fill(formTemplate, field, args, locale);
        field.setMaxCount(FormUtils.getFieldIntegerProperty(formTemplate, this.getName(), "maxCount", locale, args, null));
        field.setOptions(optionDataSource.getOptions(locale));
    }

    @Override
    public boolean isValid(MultiSelectField field, List<String> values) {
        if (CollectionUtils.isEmpty(values) && Boolean.TRUE.equals(field.getDisable())) {
            LOGGER.debug("filed [{}] is required, but it's value is empty", field.getName());
            return false;
        }
        if (values.size() > field.getMaxCount()) {
            LOGGER.debug("selected option count[{}] of filed[{}] is greater than maximum[{}]", values.size(), field.getName(), field.getMaxCount());
            return false;
        }
        if (CollectionUtils.isNotEmpty(values) && !values.stream().allMatch(value -> field.getOptions().stream().filter(option -> !Boolean.TRUE.equals(option.getDisable())).anyMatch(option -> option.getId().equals(value)))) {
            LOGGER.debug("filed [{}] is select type, but it's value[{}] dose not equal with select options", field.getName(), values);
            return false;
        }
        return true;
    }

    @Override
    public List<String> toValue(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return Arrays.stream(value.split(FormField.SEPARATOR)).map(String::trim).collect(Collectors.toList());
    }
}