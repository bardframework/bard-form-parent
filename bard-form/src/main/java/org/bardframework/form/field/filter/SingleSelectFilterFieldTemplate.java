package org.bardframework.form.field.filter;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.field.input.InputFieldTemplateAbstract;
import org.bardframework.form.field.option.OptionDataSource;
import org.bardframework.form.model.filter.IdFilter;

import java.util.Locale;
import java.util.Map;

@Getter
@Setter
public class SingleSelectFilterFieldTemplate extends InputFieldTemplateAbstract<SingleSelectFilterField, IdFilter<String>> {

    public final OptionDataSource optionDataSource;

    public SingleSelectFilterFieldTemplate(String name, OptionDataSource optionDataSource) {
        super(name);
        this.optionDataSource = optionDataSource;
    }

    @Override
    public void fill(FormTemplate formTemplate, SingleSelectFilterField field, Map<String, Object> values, Map<String, Object> args, Locale locale) throws Exception {
        super.fill(formTemplate, field, values, args, locale);
        field.setOptions(optionDataSource.getOptions(args, locale));
    }


    @Override
    public boolean isValid(String flowToken, SingleSelectFilterField field, IdFilter<String> filter, Map<String, Object> flowData) {
        if (null == filter || StringUtils.isBlank(filter.getEquals())) {
            if (Boolean.TRUE.equals(field.getRequired())) {
                log.debug("filterField [{}] is required, but it's value is empty", field.getName());
                return false;
            }
            return true;
        }
        if (field.getOptions().stream().filter(option -> !Boolean.TRUE.equals(option.getDisable())).noneMatch(option -> option.getId().equals(filter.getEquals().trim()))) {
            log.debug("field [{}] is select type, but it's value[{}] dose not equal with select options", field.getName(), filter);
            return false;
        }
        return true;
    }

    @Override
    public IdFilter<String> toValue(Object value) {
        if (null == value) {
            return null;
        }
        if (!(value instanceof String)) {
            throw new IllegalStateException(value + " is not valid for: " + this.getClass().getName());
        }
        if (StringUtils.isBlank(value.toString())) {
            return null;
        }
        return new IdFilter<String>().setEquals(value.toString());
    }
}