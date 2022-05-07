package org.bardframework.form.field;

import org.bardframework.form.common.field.NewPasswordField;
import org.bardframework.form.common.field.TextField;

public class NewPasswordFieldTemplate extends TextFieldTemplate {

    public NewPasswordFieldTemplate(String name) {
        super(name);
    }

    @Override
    public TextField getEmptyField() {
        return new NewPasswordField();
    }
}
