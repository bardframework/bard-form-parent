package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;
import org.bardframework.form.field.option.OptionDataSource;

@Getter
@Setter
public class MultiSelectSearchableFieldTemplate extends MultiSelectFieldTemplateAbstract<MultiSelectSearchableField> {

    public MultiSelectSearchableFieldTemplate(String name, OptionDataSource optionDataSource) {
        super(name, optionDataSource);
    }

    @Override
    protected MultiSelectSearchableField getEmptyField() {
        return new MultiSelectSearchableField();
    }
}