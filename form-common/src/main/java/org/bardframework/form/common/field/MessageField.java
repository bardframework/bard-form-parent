package org.bardframework.form.common.field;

import com.fasterxml.jackson.annotation.JsonTypeName;
import org.bardframework.form.common.FieldType;
import org.bardframework.form.common.FieldTypeBase;
import org.bardframework.form.common.field.base.Field;

@JsonTypeName("MESSAGE")
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