package org.bardframework.form.field;

import org.apache.commons.lang3.StringUtils;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.common.field.SingleSelectField;
import org.bardframework.form.field.base.FormFieldTemplate;
import org.bardframework.form.field.option.OptionDataSource;

import java.util.Locale;
import java.util.Map;

public class SingleSelectFieldTemplate extends FormFieldTemplate<SingleSelectField, String> {

    protected final OptionDataSource optionDataSource;

    protected SingleSelectFieldTemplate(String name, OptionDataSource optionDataSource) {
        super(name);
        this.optionDataSource = optionDataSource;
    }

    @Override
    public SingleSelectField getEmptyField() {
        return new SingleSelectField();
    }

    @Override
    public void fill(FormTemplate formTemplate, SingleSelectField field, Map<String, String> args, Locale locale) throws Exception {
        super.fill(formTemplate, field, args, locale);
        field.setOptions(optionDataSource.getOptions(locale));
    }


    @Override
    public boolean isValid(SingleSelectField field, String value) {
        if (StringUtils.isBlank(value) && Boolean.TRUE.equals(field.getDisable())) {
            LOGGER.debug("filed [{}] is required, but it's value is empty", field.getName());
            return false;
        }
        if (StringUtils.isNotBlank(value) && field.getOptions().stream().filter(option -> !Boolean.TRUE.equals(option.getDisable())).noneMatch(option -> option.getId().equals(value.trim()))) {
            LOGGER.debug("filed [{}] is select type, but it's value[{}] dose not equal with select options", field.getName(), value);
            return false;
        }
        return true;
    }

    @Override
    public String toValue(String value) {
        return value;
    }
}