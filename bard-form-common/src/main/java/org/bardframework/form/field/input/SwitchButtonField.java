package org.bardframework.form.field.input;

import org.bardframework.form.field.FieldType;

public class SwitchButtonField extends InputField<Boolean> {

    public SwitchButtonField() {
    }

    public SwitchButtonField(String name) {
        super(name);
    }

    public Boolean toValue(String value) {
        return Boolean.valueOf(value);
    }

    @Override
    public FieldType getType() {
        return InputFieldType.SWITCH_BUTTON;
    }

    @Override
    public String toString(Boolean value) {
        return null == value ? null : value.toString();
    }
}
