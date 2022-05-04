package org.bardframework.form.template;

import org.bardframework.form.FieldType;

public class DividerFieldTemplate extends FormFieldTemplate {

    public DividerFieldTemplate(String name) {
        super(name, FieldType.DIVIDER.name(), true);
    }
}
