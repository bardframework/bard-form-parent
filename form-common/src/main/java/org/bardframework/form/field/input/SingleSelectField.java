package org.bardframework.form.field.input;

import org.bardframework.form.field.InputField;
import org.bardframework.form.model.SelectOption;

import java.util.List;

public class SingleSelectField extends InputField<String> {
    private List<SelectOption> options;

    public SingleSelectField() {
    }

    public SingleSelectField(String name, List<SelectOption> options) {
        super(name);
        this.options = options;
    }

    @Override
    public String toString(String value) {
        return value;
    }

    @Override
    public InputFieldType getType() {
        return InputFieldType.SINGLE_SELECT;
    }

    public List<SelectOption> getOptions() {
        return options;
    }

    public void setOptions(List<SelectOption> options) {
        this.options = options;
    }

}
