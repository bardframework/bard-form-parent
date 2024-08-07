package org.bardframework.form.field.filter;

import lombok.Getter;
import lombok.Setter;
import org.bardframework.form.field.option.OptionDataSource;

@Getter
@Setter
public class MultiSelectSearchableFilterFieldTemplate extends MultiSelectFilterFieldTemplate {

    public MultiSelectSearchableFilterFieldTemplate(String name, OptionDataSource optionDataSource) {
        super(name, optionDataSource);
    }

    @Override
    protected MultiSelectSearchableFilterField getEmptyField() {
        return new MultiSelectSearchableFilterField();
    }
}