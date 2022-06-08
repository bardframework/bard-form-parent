package org.bardframework.form.field;

public class PasswordField extends TextField {

    public PasswordField() {
    }

    protected PasswordField(String name) {
        super(name);
    }

    @Override
    public FieldType getType() {
        return FieldType.PASSWORD;
    }
}