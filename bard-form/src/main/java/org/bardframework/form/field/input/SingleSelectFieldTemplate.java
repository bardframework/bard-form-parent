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

    public SingleSelectFieldTemplate(String name, OptionDataSource optionDataSource) {
        super(name);
        this.optionDataSource = optionDataSource;
    }

    @Override
    public void fill(FormTemplate formTemplate, SingleSelectField field, Map<String, Object> values, Map<String, Object> args, Locale locale) throws Exception {
        super.fill(formTemplate, field, values, args, locale);
        field.setOptions(optionDataSource.getOptions(args, locale));
    }

    @Override
    public boolean isValid(String flowToken, SingleSelectField field, String value, Map<String, Object> flowData) {
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
}