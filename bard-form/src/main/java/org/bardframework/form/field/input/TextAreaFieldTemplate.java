package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TextAreaFieldTemplate extends TextFieldTemplate {

    public TextAreaFieldTemplate(String name) {
        super(name);
    }

    @Override
    protected TextAreaField getEmptyField() {
        return new TextAreaField();
    }
}
