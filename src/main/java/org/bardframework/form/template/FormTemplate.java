package org.bardframework.form.template;

import java.util.List;

public class FormTemplate {

    private final String name;
    private final List<FormFieldTemplate> fields;

    public FormTemplate(String name, List<FormFieldTemplate> fields) {
        this.name = name;
        this.fields = fields;
    }

    public String getName() {
        return name;
    }

    public List<FormFieldTemplate> getFields() {
        return fields;
    }

    public FormFieldTemplate getField(String name) {
        return this.getFields().stream().filter(field -> field.getName().equals(name)).findFirst().orElse(null);
    }
}
