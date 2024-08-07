package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;
import org.bardframework.form.field.option.OptionDataSource;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
public abstract class MultiSelectFieldTemplateAbstract<F extends MultiSelectField> extends InputFieldTemplateAbstract<F, List<String>> {

    protected final OptionDataSource optionDataSource;

    public MultiSelectFieldTemplateAbstract(String name, OptionDataSource optionDataSource) {
        super(name);
        this.optionDataSource = optionDataSource;
    }

    @Override
    public void fill(FormTemplate formTemplate, F field, Map<String, String> values, Locale locale) throws Exception {
        super.fill(formTemplate, field, values, locale);
        field.setMaxCount(FormUtils.getFieldIntegerProperty(formTemplate, this, "maxCount", locale, values, this.getDefaultValue().getMaxCount()));
        field.setOptions(null == this.getOptionDataSource() ? List.of() : this.getOptionDataSource().getOptions(locale));
        field.setSubmitType(this.getDefaultValue().getSubmitType());
    }

    @Override
    public boolean isValid(String flowToken, F field, List<String> values, Map<String, String> flowData) {
        if (CollectionUtils.isEmpty(values)) {
            if (Boolean.TRUE.equals(field.getRequired())) {
                log.debug("field [{}] is required, but it's value is empty", field.getName());
                return false;
            }
            return true;
        }
        if (values.size() > field.getMaxCount()) {
            log.debug("selected option count[{}] of field[{}] is greater than maximum[{}]", values.size(), field.getName(), field.getMaxCount());
            return false;
        }
        if (!values.stream().allMatch(value -> field.getOptions().stream().filter(option -> !Boolean.TRUE.equals(option.getDisable())).anyMatch(option -> option.getId().equals(value)))) {
            log.debug("field [{}] is select type, but it's value[{}] dose not equal with select options", field.getName(), values);
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