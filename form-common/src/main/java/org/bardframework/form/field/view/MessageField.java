package org.bardframework.form.field.view;

import org.bardframework.form.field.Field;

public class MessageField extends Field {

    private String message;

    @Override
    public ViewFieldType getType() {
        return ViewFieldType.MESSAGE;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}