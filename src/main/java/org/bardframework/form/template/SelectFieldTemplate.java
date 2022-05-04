package org.bardframework.form.template;

import org.bardframework.form.FieldType;
import org.bardframework.form.model.SelectOptionsDataProvider;

public class SelectFieldTemplate extends FormFieldTemplate {

    public SelectFieldTemplate(String name, SelectOptionsDataProvider dataProvider) {
        super(name, FieldType.SELECT.name());
        this.setOptionsDataProvider(dataProvider);
    }
}
