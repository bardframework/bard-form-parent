package org.bardframework.form.common.field;

import org.bardframework.form.common.FieldType;
import org.bardframework.form.common.FieldTypeBase;
import org.bardframework.form.common.field.base.Field;

public class MessageField extends Field {

    private String message;

    @Override
    public FieldType<?> getType() {
        return FieldTypeBase.MESSAGE;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}