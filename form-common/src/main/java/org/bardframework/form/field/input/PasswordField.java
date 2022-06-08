package org.bardframework.form.field.input;

public class PasswordField extends TextField {

    public PasswordField() {
    }

    protected PasswordField(String name) {
        super(name);
    }

    @Override
    public InputFieldType getType() {
        return InputFieldType.PASSWORD;
    }
}