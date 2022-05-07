package org.bardframework.form.common.field;

import org.bardframework.form.common.FieldType;
import org.bardframework.form.common.FieldTypeBase;

public class PasswordField extends TextField {

    public PasswordField() {
    }

    protected PasswordField(String name) {
        super(name);
    }

    @Override
    public FieldType<?> getType() {
        return FieldTypeBase.PASSWORD;
    }
}