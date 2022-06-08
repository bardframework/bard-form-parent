package org.bardframework.form.table.filter.field;

import org.apache.commons.lang3.StringUtils;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.field.base.FormFieldTemplate;
import org.bardframework.form.field.option.OptionDataSource;
import org.bardframework.form.filter.IdFilter;
import org.bardframework.form.filter.field.SingleSelectFilterField;

import java.util.Locale;
import java.util.Map;

public class SingleSelectFilterFieldTemplate extends FormFieldTemplate<SingleSelectFilterField, IdFilter<String>> {

    protected final OptionDataSource optionDataSource;

    protected SingleSelectFilterFieldTemplate(String name, OptionDataSource optionDataSource) {
        super(name);
        this.optionDataSource = optionDataSource;
    }

    @Override
    public void fill(FormTemplate formTemplate, SingleSelectFilterField field, Map<String, String> args, Locale locale) throws Exception {
        super.fill(formTemplate, field, args, locale);
        field.setOptions(optionDataSource.getOptions(locale));
    }


    @Override
    public boolean isValid(SingleSelectFilterField field, IdFilter<String> filter) {
        if (null == filter || StringUtils.isBlank(filter.getEquals())) {
            if (Boolean.TRUE.equals(field.getRequired())) {
                LOGGER.debug("filterField [{}] is required, but it's value is empty", field.getName());
                return false;
            }
            return true;
        }
        if (field.getOptions().stream().filter(option -> !Boolean.TRUE.equals(option.getDisable())).noneMatch(option -> option.getId().equals(filter.getEquals().trim()))) {
            LOGGER.debug("field [{}] is select type, but it's value[{}] dose not equal with select options", field.getName(), filter);
            return false;
        }
        return true;
    }

    @Override
    public IdFilter<String> toValue(String id) {
        return StringUtils.isBlank(id) ? null : new IdFilter<String>().setEquals(id);
    }
}