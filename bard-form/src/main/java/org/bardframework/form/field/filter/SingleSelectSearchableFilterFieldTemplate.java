package org.bardframework.form.field.filter;

import org.bardframework.form.field.option.OptionDataSource;

public class SingleSelectSearchableFilterFieldTemplate extends SingleSelectFilterFieldTemplate {

    protected SingleSelectSearchableFilterFieldTemplate(String name, OptionDataSource optionDataSource) {
        super(name, optionDataSource);
    }

    @Override
    protected SingleSelectSearchableFilterField getEmptyField() {
        return new SingleSelectSearchableFilterField();
    }
}