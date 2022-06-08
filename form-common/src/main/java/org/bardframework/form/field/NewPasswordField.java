package org.bardframework.form.field;

public class NewPasswordField extends PasswordField {

    @Override
    public FieldType getType() {
        return FieldType.NEW_PASSWORD;
    }
}