package org.bardframework.form.field.filter;

import lombok.Getter;
import lombok.Setter;
import org.bardframework.form.field.option.OptionDataSource;

@Getter
@Setter
public class SingleSelectSearchableFilterFieldTemplate extends SingleSelectFilterFieldTemplate {

    public SingleSelectSearchableFilterFieldTemplate(String name, OptionDataSource optionDataSource) {
        super(name, optionDataSource);
    }

    @Override
    protected SingleSelectSearchableFilterField getEmptyField() {
        return new SingleSelectSearchableFilterField();
    }
}