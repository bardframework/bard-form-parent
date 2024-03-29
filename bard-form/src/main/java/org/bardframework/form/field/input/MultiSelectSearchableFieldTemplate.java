package org.bardframework.form.field.input;

import org.bardframework.form.field.option.OptionDataSource;

public class MultiSelectSearchableFieldTemplate extends MultiSelectFieldTemplate {

    protected MultiSelectSearchableFieldTemplate(String name, OptionDataSource optionDataSource) {
        super(name, optionDataSource);
    }

    @Override
    protected MultiSelectSearchableField getEmptyField() {
        return new MultiSelectSearchableField();
    }
}