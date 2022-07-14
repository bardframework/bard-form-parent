package org.bardframework.form.field.input;

import org.bardframework.form.field.option.OptionDataSource;

public class SingleSelectSearchableFieldTemplate extends SingleSelectFieldTemplate {

    protected SingleSelectSearchableFieldTemplate(String name, OptionDataSource optionDataSource) {
        super(name, optionDataSource);
    }

    @Override
    protected SingleSelectField getEmptyField() {
        return new SingleSelectSearchableField();
    }
}