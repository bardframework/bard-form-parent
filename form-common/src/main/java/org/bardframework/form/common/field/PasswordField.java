package org.bardframework.form.common.field;

import com.fasterxml.jackson.annotation.JsonTypeName;
import org.bardframework.form.common.FieldType;
import org.bardframework.form.common.FieldTypeBase;

@JsonTypeName("PASSWORD")
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