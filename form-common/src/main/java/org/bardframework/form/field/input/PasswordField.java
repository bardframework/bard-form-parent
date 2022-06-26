package org.bardframework.form.field.input;

import org.bardframework.form.field.FieldType;

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
    public FieldType getType() {
        return InputFieldType.PASSWORD;
    }
}