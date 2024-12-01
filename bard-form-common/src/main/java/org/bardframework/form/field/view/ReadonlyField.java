package org.bardframework.form.field.view;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bardframework.form.field.Field;
import org.bardframework.form.field.FieldType;

@Getter
@Setter
@ToString
public class ReadonlyField extends Field {

    private String name;
    private String mask;
    private Object value;

    @Override
    public FieldType getType() {
        return ViewFieldType.READONLY;
    }
}