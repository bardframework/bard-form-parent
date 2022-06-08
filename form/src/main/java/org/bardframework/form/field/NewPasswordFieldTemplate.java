package org.bardframework.form.field;

public class NewPasswordFieldTemplate extends TextFieldTemplate {

    public NewPasswordFieldTemplate(String name) {
        super(name);
    }

    @Override
    protected NewPasswordField getEmptyField() {
        return new NewPasswordField();
    }
}
