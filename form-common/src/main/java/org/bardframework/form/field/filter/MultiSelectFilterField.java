package org.bardframework.form.field.filter;

import org.bardframework.form.field.InputField;
import org.bardframework.form.model.SelectOption;
import org.bardframework.form.model.filter.IdFilter;

import java.util.List;

public class MultiSelectFilterField extends InputField<IdFilter<String>> {
    private Integer maxCount;
    private List<SelectOption> options;

    public MultiSelectFilterField() {
    }

    public MultiSelectFilterField(String name, List<SelectOption> options) {
        super(name);
        this.options = options;
    }

    @Override
    public FilterFieldType getType() {
        return FilterFieldType.MULTI_SELECT_FILTER;
    }

    @Override
    public String toString(IdFilter<String> value) {
        return null == value ? null : String.join(SEPARATOR, value.getIn());
    }

    public Integer getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(Integer maxCount) {
        this.maxCount = maxCount;
    }

    public List<SelectOption> getOptions() {
        return options;
    }

    public void setOptions(List<SelectOption> options) {
        this.options = options;
    }
}
