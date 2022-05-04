package org.bardframework.form.template;

import org.bardframework.form.FieldType;

public class DisabledTextFieldTemplate extends FormFieldTemplate {

    public DisabledTextFieldTemplate(String name) {
        super(name, FieldType.TEXT.name(), true);
    }
}
