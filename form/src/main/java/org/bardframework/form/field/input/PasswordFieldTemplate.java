package org.bardframework.form.field.input;

public class PasswordFieldTemplate extends TextFieldTemplate {

    public PasswordFieldTemplate(String name) {
        super(name);
    }

    @Override
    protected PasswordField getEmptyField() {
        return new PasswordField();
    }
}
