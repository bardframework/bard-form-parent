package org.bardframework.form.field.input;

import org.bardframework.form.field.InputField;
import org.bardframework.form.model.SelectOption;

import java.util.List;

public class MultiSelectField extends InputField<List<String>> {
    private Integer maxCount;
    private List<SelectOption> options;

    public MultiSelectField() {
    }

    public MultiSelectField(String name, List<SelectOption> options) {
        super(name);
        this.options = options;
    }

    @Override
    public InputFieldType getType() {
        return InputFieldType.MULTI_SELECT;
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
