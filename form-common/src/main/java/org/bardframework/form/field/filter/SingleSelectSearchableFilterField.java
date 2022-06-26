package org.bardframework.form.field.filter;

import org.bardframework.form.field.FieldType;
import org.bardframework.form.model.SelectOption;

import java.util.List;

public class SingleSelectSearchableFilterField extends SingleSelectFilterField {

    public SingleSelectSearchableFilterField() {
    }

    public SingleSelectSearchableFilterField(String name, List<SelectOption> options) {
        super(name, options);
    }

    @Override
    public FieldType getType() {
        return FilterFieldType.SINGLE_SELECT_SEARCHABLE_FILTER;
    }

}
