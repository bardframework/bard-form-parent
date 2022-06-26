package org.bardframework.form.field.view;

import org.bardframework.form.field.Field;
import org.bardframework.form.field.FieldType;

public class DividerField extends Field {

    @Override
    public FieldType getType() {
        return ViewFieldType.DIVIDER;
    }

}
