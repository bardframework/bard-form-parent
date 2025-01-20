package org.bardframework.form.field.filter;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;
import org.bardframework.form.field.input.InputFieldTemplateAbstract;
import org.bardframework.form.field.option.OptionDataSource;
import org.bardframework.form.model.filter.IdFilter;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@Getter
@Setter
public class MultiSelectFilterFieldTemplate extends InputFieldTemplateAbstract<MultiSelectFilterField, IdFilter<String>> {

    protected final OptionDataSource optionDataSource;

    public MultiSelectFilterFieldTemplate(String name, OptionDataSource optionDataSource) {
        super(name);
        this.optionDataSource = optionDataSource;
    }

    @Override
    public void fill(FormTemplate formTemplate, MultiSelectFilterField filterField, Map<String, Object> values, Map<String, Object> args, Locale locale) throws Exception {
        super.fill(formTemplate, filterField, values, args, locale);
        filterField.setMaxCount(FormUtils.getFieldIntegerProperty(formTemplate, this, "maxCount", locale, args, this.getDefaultValue().getMaxCount()));
        filterField.setOptions(optionDataSource.getOptions(values, args, locale));
    }

    @Override
    public boolean isValid(String flowToken, MultiSelectFilterField filterField, IdFilter<String> idFilter, Map<String, Object> flowData) {
        if (null == idFilter || CollectionUtils.isEmpty(idFilter.getIn())) {
            if (Boolean.TRUE.equals(filterField.getRequired())) {
                log.debug("filterField [{}] is required, but it's value is empty", filterField.getName());
                return false;
            }
            return true;
        }
        if (idFilter.getIn().size() > filterField.getMaxCount()) {
            log.debug("selected option count[{}] of filterField[{}] is greater than maximum[{}]", idFilter.getIn().size(), filterField.getName(), filterField.getMaxCount());
            return false;
        }
        if (!idFilter.getIn().stream().allMatch(value -> filterField.getOptions().stream().filter(option -> !Boolean.TRUE.equals(option.getDisable())).anyMatch(option -> option.getId().equals(value)))) {
            log.debug("filterField [{}] is select type, but it's value[{}] dose not equal with select options", filterField.getName(), idFilter);
            return false;
        }
        return true;
    }

    @Override
    public IdFilter<String> toValue(Object value) {
        if (null == value) {
            return null;
        }
        if (!(value instanceof List<?>)) {
            throw new IllegalStateException(value + " is not valid for: " + this.getClass().getName());
        }
        List<String> parts = (List<String>) value;
        if (parts.isEmpty()) {
            return null;
        }
        return new IdFilter<String>().setIn(parts);
    }
}