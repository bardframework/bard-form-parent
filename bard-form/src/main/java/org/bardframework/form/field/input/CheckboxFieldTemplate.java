package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.bardframework.form.field.option.OptionDataSource;

@Slf4j
@Getter
public class CheckboxFieldTemplate extends MultiSelectFieldTemplate {

    protected CheckboxFieldTemplate(String name, OptionDataSource optionDataSource) {
        super(name, optionDataSource);
    }

    @Override
    protected CheckBoxField getEmptyField() {
        return new CheckBoxField();
    }

}