package org.bardframework.form.field.view;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.bardframework.form.field.Field;
import org.bardframework.form.field.FieldType;

@Slf4j
@Getter
@Setter
@ToString
public class MessageField extends Field {

    private String message;

    @Override
    public FieldType getType() {
        return ViewFieldType.MESSAGE;
    }
}