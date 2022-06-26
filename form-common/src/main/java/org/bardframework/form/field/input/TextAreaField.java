package org.bardframework.form.field.input;

import org.bardframework.form.field.FieldType;

public class TextAreaField extends TextField {
    public TextAreaField() {
    }

    public TextAreaField(String name) {
        super(name);
    }

    @Override
    public FieldType getType() {
        return InputFieldType.TEXT_AREA;
    }

}
