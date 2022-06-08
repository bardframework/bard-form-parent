package org.bardframework.form.field;

import org.bardframework.form.common.FormField;
import org.bardframework.form.common.SelectOption;

import java.util.List;

public class MultiSelectField extends FormField<List<String>> {
    private Integer maxCount;
    private List<SelectOption> options;

    public MultiSelectField() {
    }

    public MultiSelectField(String name, List<SelectOption> options) {
        super(name);
        this.options = options;
    }

    @Override
    public FieldType getType() {
        return FieldType.MULTI_SELECT;
    }

    @Override
    public String toString(List<String> value) {
        return null == value ? null : String.join(SEPARATOR, value);
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
