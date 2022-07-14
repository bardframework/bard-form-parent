package org.bardframework.form.field.input;

import org.bardframework.form.field.option.OptionDataSource;

public class CheckboxFieldTemplate extends MultiSelectFieldTemplate {

    protected CheckboxFieldTemplate(String name, OptionDataSource optionDataSource) {
        super(name, optionDataSource);
    }

    @Override
    protected CheckBoxField getEmptyField() {
        return new CheckBoxField();
    }

}