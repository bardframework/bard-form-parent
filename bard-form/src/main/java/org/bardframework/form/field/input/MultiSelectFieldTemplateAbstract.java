package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;
import org.bardframework.form.field.option.OptionDataSource;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@Getter
@Setter
public abstract class MultiSelectFieldTemplateAbstract<F extends MultiSelectField> extends InputFieldTemplateAbstract<F, List<String>> {

    protected final OptionDataSource optionDataSource;

    public MultiSelectFieldTemplateAbstract(String name, OptionDataSource optionDataSource) {
        super(name);
        this.optionDataSource = optionDataSource;
    }

    @Override
    public void fill(FormTemplate formTemplate, F field, Map<String, Object> values, Map<String, Object> args, Locale locale) throws Exception {
        super.fill(formTemplate, field, values, args, locale);
        field.setMinCount(FormUtils.getFieldIntegerProperty(formTemplate, this, "minCount", locale, args, this.getDefaultValue().getMinCount()));
        field.setMaxCount(FormUtils.getFieldIntegerProperty(formTemplate, this, "maxCount", locale, args, this.getDefaultValue().getMaxCount()));
        field.setOptions(null == this.getOptionDataSource() ? List.of() : this.getOptionDataSource().getOptions(values, args, locale));
    }

    @Override
    public boolean isValid(String flowToken, F field, List<String> values, Map<String, Object> flowData) {
        if (CollectionUtils.isEmpty(values)) {
            if (Boolean.TRUE.equals(field.getRequired())) {
                log.debug("field [{}] is required, but it's value is empty", field.getName());
                return false;
            }
            return true;
        }
        if (null != field.getMaxCount() && values.size() > field.getMaxCount()) {
            log.debug("selected option count[{}] of field[{}] is more than maximum[{}]", values.size(), field.getName(), field.getMaxCount());
            return false;
        }
        if (null != field.getMinCount() && values.size() < field.getMinCount()) {
            log.debug("selected option count[{}] of field[{}] is less than maximum[{}]", values.size(), field.getName(), field.getMinCount());
            return false;
        }
        if (!values.stream().allMatch(value -> field.getOptions().stream().filter(option -> !Boolean.TRUE.equals(option.getDisable())).anyMatch(option -> option.getId().equals(value)))) {
            log.debug("field [{}] is select type, but it's value[{}] dose not equal with select options", field.getName(), values);
            return false;
        }
        return true;
    }
}