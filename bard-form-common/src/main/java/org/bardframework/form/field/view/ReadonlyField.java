package org.bardframework.form.field.view;

import org.bardframework.form.field.Field;
import org.bardframework.form.field.FieldType;

public class ReadonlyField extends Field {

    private String mask;
    private String value;

    @Override
    public FieldType getType() {
        return ViewFieldType.READONLY;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}