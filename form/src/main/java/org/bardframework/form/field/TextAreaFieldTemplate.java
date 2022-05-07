package org.bardframework.form.field;

import org.bardframework.form.common.field.TextAreaField;
import org.bardframework.form.common.field.TextField;

public class TextAreaFieldTemplate extends TextFieldTemplate {

    public TextAreaFieldTemplate(String name) {
        super(name);
    }

    @Override
    public TextField getEmptyField() {
        return new TextAreaField();
    }
}
