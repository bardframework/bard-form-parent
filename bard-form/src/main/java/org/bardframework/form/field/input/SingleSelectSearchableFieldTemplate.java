package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;
import org.bardframework.form.field.option.OptionDataSource;

@Getter
@Setter
public class SingleSelectSearchableFieldTemplate extends SingleSelectFieldTemplate {

    public SingleSelectSearchableFieldTemplate(String name, OptionDataSource optionDataSource) {
        super(name, optionDataSource);
    }

    @Override
    protected SingleSelectField getEmptyField() {
        return new SingleSelectSearchableField();
    }
}