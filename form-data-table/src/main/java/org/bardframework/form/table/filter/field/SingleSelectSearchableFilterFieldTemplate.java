package org.bardframework.form.table.filter.field;

import org.bardframework.form.field.option.OptionDataSource;
import org.bardframework.form.filter.field.SingleSelectSearchableFilterField;

public class SingleSelectSearchableFilterFieldTemplate extends SingleSelectFilterFieldTemplate {

    protected SingleSelectSearchableFilterFieldTemplate(String name, OptionDataSource optionDataSource) {
        super(name, optionDataSource);
    }

    @Override
    protected SingleSelectSearchableFilterField getEmptyField() {
        return new SingleSelectSearchableFilterField();
    }
}