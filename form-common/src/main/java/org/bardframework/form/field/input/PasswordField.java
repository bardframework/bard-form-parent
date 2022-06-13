package org.bardframework.form.field.input;

import org.bardframework.form.field.InputField;

public class PasswordField extends InputField<String> {

    public PasswordField() {
    }

    protected PasswordField(String name) {
        super(name);
    }

    @Override
    public String toString(String value) {
        return value;
    }

    @Override
    public InputFieldType getType() {
        return InputFieldType.PASSWORD;
    }
}