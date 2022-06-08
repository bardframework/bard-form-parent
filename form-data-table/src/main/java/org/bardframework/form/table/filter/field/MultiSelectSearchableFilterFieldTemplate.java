package org.bardframework.form.table.filter.field;

import org.bardframework.form.field.option.OptionDataSource;
import org.bardframework.form.filter.field.MultiSelectSearchableFilterField;

public class MultiSelectSearchableFilterFieldTemplate extends MultiSelectFilterFieldTemplate {

    protected MultiSelectSearchableFilterFieldTemplate(String name, OptionDataSource optionDataSource) {
        super(name, optionDataSource);
    }

    @Override
    protected MultiSelectSearchableFilterField getEmptyField() {
        return new MultiSelectSearchableFilterField();
    }
}