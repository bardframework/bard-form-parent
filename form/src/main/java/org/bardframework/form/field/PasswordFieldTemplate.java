package org.bardframework.form.field;

import org.bardframework.form.common.field.PasswordField;
import org.bardframework.form.common.field.TextField;

public class PasswordFieldTemplate extends TextFieldTemplate {

    public PasswordFieldTemplate(String name) {
        super(name);
    }

    @Override
    public TextField getEmptyField() {
        return new PasswordField();
    }
}
