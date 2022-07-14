package org.bardframework.form.field.input;

import org.bardframework.form.field.FieldType;

public class NewPasswordField extends TextField {

    @Override
    public FieldType getType() {
        return InputFieldType.NEW_PASSWORD;
    }
}