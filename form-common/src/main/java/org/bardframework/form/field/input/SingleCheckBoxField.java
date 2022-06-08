package org.bardframework.form.field.input;

import org.bardframework.form.field.InputField;

public class SingleCheckBoxField extends InputField<Boolean> {

    public SingleCheckBoxField() {
    }

    public SingleCheckBoxField(String name) {
        super(name);
    }

    public Boolean toValue(String value) {
        return Boolean.valueOf(value);
    }

    @Override
    public InputFieldType getType() {
        return InputFieldType.SINGLE_CHECKBOX;
    }

    @Override
    public String toString(Boolean value) {
        return null == value ? null : value.toString();
    }
}
