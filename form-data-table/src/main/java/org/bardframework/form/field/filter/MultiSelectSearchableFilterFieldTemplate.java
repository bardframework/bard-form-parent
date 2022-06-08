package org.bardframework.form.field.filter;

import org.bardframework.form.field.option.OptionDataSource;

public class MultiSelectSearchableFilterFieldTemplate extends MultiSelectFilterFieldTemplate {

    protected MultiSelectSearchableFilterFieldTemplate(String name, OptionDataSource optionDataSource) {
        super(name, optionDataSource);
    }

    @Override
    protected MultiSelectSearchableFilterField getEmptyField() {
        return new MultiSelectSearchableFilterField();
    }
}