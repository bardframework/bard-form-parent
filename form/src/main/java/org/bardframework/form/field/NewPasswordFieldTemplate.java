package org.bardframework.form.field;

import org.bardframework.form.common.field.NewPasswordField;

public class NewPasswordFieldTemplate extends TextFieldTemplate {

    public NewPasswordFieldTemplate(String name) {
        super(name);
    }

    @Override
    protected NewPasswordField getEmptyField() {
        return new NewPasswordField();
    }
}
