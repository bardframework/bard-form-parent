package org.bardframework.form.field;

import org.bardframework.form.common.FormField;
import org.bardframework.form.common.SelectOption;

import java.util.List;

public class SingleSelectField extends FormField<String> {
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
    public FieldType getType() {
        return FieldType.SINGLE_SELECT;
    }

    public List<SelectOption> getOptions() {
        return options;
    }

    public void setOptions(List<SelectOption> options) {
        this.options = options;
    }

}
