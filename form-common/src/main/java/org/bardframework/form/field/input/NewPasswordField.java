package org.bardframework.form.field.input;

public class NewPasswordField extends PasswordField {

    @Override
    public InputFieldType getType() {
        return InputFieldType.NEW_PASSWORD;
    }
}