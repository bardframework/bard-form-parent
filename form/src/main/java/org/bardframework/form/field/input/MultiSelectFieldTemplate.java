package org.bardframework.form.field.input;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;
import org.bardframework.form.field.option.OptionDataSource;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class MultiSelectFieldTemplate extends InputFieldTemplate<MultiSelectField, List<String>> {

    protected final OptionDataSource optionDataSource;

    protected MultiSelectFieldTemplate(String name, OptionDataSource optionDataSource) {
        super(name);
        this.optionDataSource = optionDataSource;
    }

    @Override
    public void fill(FormTemplate formTemplate, MultiSelectField field, Map<String, String> args, Locale locale, HttpServletRequest httpRequest) throws Exception {
        super.fill(formTemplate, field, args, locale, httpRequest);
        field.setMaxCount(FormUtils.getFieldIntegerProperty(formTemplate, this, "maxCount", locale, args, null));
        field.setOptions(optionDataSource.getOptions(locale));
    }

    @Override
    public boolean isValid(MultiSelectField field, List<String> values) {
        if (CollectionUtils.isEmpty(values)) {
            if (Boolean.TRUE.equals(field.getRequired())) {
                LOGGER.debug("field [{}] is required, but it's value is empty", field.getName());
                return false;
            }
            return true;
        }
        if (values.size() > field.getMaxCount()) {
            LOGGER.debug("selected option count[{}] of field[{}] is greater than maximum[{}]", values.size(), field.getName(), field.getMaxCount());
            return false;
        }
        if (!values.stream().allMatch(value -> field.getOptions().stream().filter(option -> !Boolean.TRUE.equals(option.getDisable())).anyMatch(option -> option.getId().equals(value)))) {
            LOGGER.debug("field [{}] is select type, but it's value[{}] dose not equal with select options", field.getName(), values);
            return false;
        }
        return true;
    }

    @Override
    public List<String> toValue(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return Arrays.stream(value.split(InputField.SEPARATOR)).map(String::trim).collect(Collectors.toList());
    }
}