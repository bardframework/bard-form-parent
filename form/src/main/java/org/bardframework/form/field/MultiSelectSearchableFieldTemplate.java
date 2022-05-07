package org.bardframework.form.field;

import org.bardframework.form.common.field.MultiSelectField;
import org.bardframework.form.field.option.OptionDataSource;

public class MultiSelectSearchableFieldTemplate extends MultiSelectFieldTemplate {

    protected MultiSelectSearchableFieldTemplate(String name, OptionDataSource optionDataSource) {
        super(name, optionDataSource);
    }

    @Override
    public MultiSelectField getEmptyField() {
        return new MultiSelectField();
    }
}