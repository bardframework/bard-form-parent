package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.field.option.OptionDataSource;

import java.util.Locale;
import java.util.Map;

@Getter
@Setter
public class SingleSelectFieldTemplate extends InputFieldTemplateAbstract<SingleSelectField, String> {

    public final OptionDataSource optionDataSource;

    protected SingleSelectFieldTemplate(String name, OptionDataSource optionDataSource) {
        super(name);
        this.optionDataSource = optionDataSource;
    }

    @Override
    public void fill(FormTemplate formTemplate, SingleSelectField field, Map<String, String> values, Locale locale) throws Exception {
        super.fill(formTemplate, field, values, locale);
        field.setOptions(optionDataSource.getOptions(locale));
    }

    @Override
    public boolean isValid(String flowToken, SingleSelectField field, String value, Map<String, String> flowData) {
        if (StringUtils.isBlank(value)) {
            if (Boolean.TRUE.equals(field.getRequired())) {
                log.debug("field [{}] is required, but it's value is empty", field.getName());
                return false;
            }
            return true;
        }
        if (field.getOptions().stream().filter(option -> !Boolean.TRUE.equals(option.getDisable())).noneMatch(option -> option.getId().equals(value.trim()))) {
            log.debug("field [{}] is select type, but it's value[{}] dose not equal with select options", field.getName(), value);
            return false;
        }
        return true;
    }

    @Override
    public String toValue(String value) {
        return value;
    }
}