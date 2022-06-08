package org.bardframework.form.field;

import org.bardframework.form.common.Field;

public class MessageField extends Field {

    private String message;

    @Override
    public FieldType getType() {
        return FieldType.MESSAGE;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}