package org.bardframework.form.template;

import org.bardframework.form.FieldType;

public class TextFieldTemplate extends FormFieldTemplate {

    public TextFieldTemplate(String name) {
        super(name, FieldType.TEXT.name());
    }

    public TextFieldTemplate(String name, boolean volatile_) {
        super(name, FieldType.TEXT.name(), volatile_);
    }
}
