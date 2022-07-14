package org.bardframework.form.field.input;

import org.bardframework.form.field.FieldType;

public class CheckBoxField extends MultiSelectField {

    @Override
    public FieldType getType() {
        return InputFieldType.CHECKBOX;
    }

}
