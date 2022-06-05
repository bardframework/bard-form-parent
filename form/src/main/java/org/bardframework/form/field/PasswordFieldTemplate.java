package org.bardframework.form.field;

import org.bardframework.form.common.field.PasswordField;

public class PasswordFieldTemplate extends TextFieldTemplate {

    public PasswordFieldTemplate(String name) {
        super(name);
    }

    @Override
    protected PasswordField getEmptyField() {
        return new PasswordField();
    }
}
