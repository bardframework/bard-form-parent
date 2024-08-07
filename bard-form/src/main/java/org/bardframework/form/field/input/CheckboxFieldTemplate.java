package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;
import org.bardframework.form.field.option.OptionDataSource;

@Getter
@Setter
public class CheckboxFieldTemplate extends MultiSelectFieldTemplate {

    public CheckboxFieldTemplate(String name, OptionDataSource optionDataSource) {
        super(name, optionDataSource);
    }

    @Override
    protected CheckBoxField getEmptyField() {
        return new CheckBoxField();
    }

}