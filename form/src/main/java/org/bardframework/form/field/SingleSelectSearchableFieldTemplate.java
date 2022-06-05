package org.bardframework.form.field;

import org.bardframework.form.common.field.SingleSelectField;
import org.bardframework.form.common.field.SingleSelectSearchableField;
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