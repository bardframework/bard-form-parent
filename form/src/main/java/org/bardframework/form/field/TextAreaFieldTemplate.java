package org.bardframework.form.field;

import org.bardframework.form.common.field.TextAreaField;

public class TextAreaFieldTemplate extends TextFieldTemplate {

    public TextAreaFieldTemplate(String name) {
        super(name);
    }

    @Override
    protected TextAreaField getEmptyField() {
        return new TextAreaField();
    }
}
