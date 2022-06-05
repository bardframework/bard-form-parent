package org.bardframework.form.common.field;

import org.bardframework.form.common.FieldType;
import org.bardframework.form.common.FieldTypeBase;

public class NewPasswordField extends TextField {

    @Override
    public FieldType<?> getType() {
        return FieldTypeBase.NEW_PASSWORD;
    }
}