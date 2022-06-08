package org.bardframework.form.field.input;

public class TextAreaField extends TextField {
    public TextAreaField() {
    }

    public TextAreaField(String name) {
        super(name);
    }

    @Override
    public InputFieldType getType() {
        return InputFieldType.TEXT_AREA;
    }

}
