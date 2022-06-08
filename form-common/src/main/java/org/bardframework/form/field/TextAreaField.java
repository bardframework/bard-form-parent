package org.bardframework.form.field;

public class TextAreaField extends TextField {
    public TextAreaField() {
    }

    public TextAreaField(String name) {
        super(name);
    }

    @Override
    public FieldType getType() {
        return FieldType.TEXT_AREA;
    }

}
