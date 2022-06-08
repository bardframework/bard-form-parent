package org.bardframework.form.field;

public class PasswordFieldTemplate extends TextFieldTemplate {

    public PasswordFieldTemplate(String name) {
        super(name);
    }

    @Override
    protected PasswordField getEmptyField() {
        return new PasswordField();
    }
}
