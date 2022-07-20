package org.bardframework.form.field.input;

import org.apache.commons.lang3.StringUtils;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.field.option.OptionDataSource;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Map;

public class SingleSelectFieldTemplate extends InputFieldTemplate<SingleSelectField, String> {

    protected final OptionDataSource optionDataSource;

    protected SingleSelectFieldTemplate(String name, OptionDataSource optionDataSource) {
        super(name);
        this.optionDataSource = optionDataSource;
    }

    @Override
    public void fill(FormTemplate formTemplate, SingleSelectField field, Map<String, String> args, Locale locale, HttpServletRequest httpRequest) throws Exception {
        super.fill(formTemplate, field, args, locale, httpRequest);
        field.setOptions(optionDataSource.getOptions(locale));
    }


    @Override
    public boolean isValid(SingleSelectField field, String value, Map<String, String> args) {
        if (StringUtils.isBlank(value)) {
            if (Boolean.TRUE.equals(field.getRequired())) {
                LOGGER.debug("field [{}] is required, but it's value is empty", field.getName());
                return false;
            }
            return true;
        }
        if (field.getOptions().stream().filter(option -> !Boolean.TRUE.equals(option.getDisable())).noneMatch(option -> option.getId().equals(value.trim()))) {
            LOGGER.debug("field [{}] is select type, but it's value[{}] dose not equal with select options", field.getName(), value);
            return false;
        }
        return true;
    }

    @Override
    public String toValue(String value) {
        return value;
    }
}