package org.bardframework.form.field.filter;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;
import org.bardframework.form.field.input.InputField;
import org.bardframework.form.field.input.InputFieldTemplate;
import org.bardframework.form.field.option.OptionDataSource;
import org.bardframework.form.model.filter.IdFilter;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class MultiSelectFilterFieldTemplate extends InputFieldTemplate<MultiSelectFilterField, IdFilter<String>> {

    protected final OptionDataSource optionDataSource;

    protected MultiSelectFilterFieldTemplate(String name, OptionDataSource optionDataSource) {
        super(name);
        this.optionDataSource = optionDataSource;
    }

    @Override
    public void fill(FormTemplate formTemplate, MultiSelectFilterField filterField, Map<String, String> args, Locale locale, HttpServletRequest httpRequest) throws Exception {
        super.fill(formTemplate, filterField, args, locale, httpRequest);
        filterField.setMaxCount(FormUtils.getFieldIntegerProperty(formTemplate, this, "maxCount", locale, args, this.getDefaultValue().getMaxCount()));
        filterField.setOptions(optionDataSource.getOptions(locale));
    }

    @Override
    public boolean isValid(MultiSelectFilterField filterField, IdFilter<String> idFilter) {
        if (null == idFilter || CollectionUtils.isEmpty(idFilter.getIn())) {
            if (Boolean.TRUE.equals(filterField.getRequired())) {
                LOGGER.debug("filterField [{}] is required, but it's value is empty", filterField.getName());
                return false;
            }
            return true;
        }
        if (idFilter.getIn().size() > filterField.getMaxCount()) {
            LOGGER.debug("selected option count[{}] of filterField[{}] is greater than maximum[{}]", idFilter.getIn().size(), filterField.getName(), filterField.getMaxCount());
            return false;
        }
        if (!idFilter.getIn().stream().allMatch(value -> filterField.getOptions().stream().filter(option -> !Boolean.TRUE.equals(option.getDisable())).anyMatch(option -> option.getId().equals(value)))) {
            LOGGER.debug("filterField [{}] is select type, but it's value[{}] dose not equal with select options", filterField.getName(), idFilter);
            return false;
        }
        return true;
    }

    @Override
    public IdFilter<String> toValue(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return new IdFilter<String>().setIn(Arrays.stream(value.split(InputField.SEPARATOR)).map(String::trim).collect(Collectors.toList()));
    }
}