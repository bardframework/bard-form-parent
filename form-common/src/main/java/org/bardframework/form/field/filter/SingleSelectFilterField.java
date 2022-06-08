package org.bardframework.form.field.filter;

import org.bardframework.form.field.InputField;
import org.bardframework.form.model.SelectOption;
import org.bardframework.form.model.filter.IdFilter;

import java.util.List;

public class SingleSelectFilterField extends InputField<IdFilter<String>> {
    private List<SelectOption> options;

    public SingleSelectFilterField() {
    }

    public SingleSelectFilterField(String name, List<SelectOption> options) {
        super(name);
        this.options = options;
    }

    @Override
    public String toString(IdFilter<String> value) {
        return null == value ? null : value.getEquals();
    }

    @Override
    public FilterFieldType getType() {
        return FilterFieldType.SINGLE_SELECT_FILTER;
    }

    public List<SelectOption> getOptions() {
        return options;
    }

    public void setOptions(List<SelectOption> options) {
        this.options = options;
    }

}
