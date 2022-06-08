package org.bardframework.form.field.input;

public class TextAreaFieldTemplate extends TextFieldTemplate {

    public TextAreaFieldTemplate(String name) {
        super(name);
    }

    @Override
    protected TextAreaField getEmptyField() {
        return new TextAreaField();
    }
}
