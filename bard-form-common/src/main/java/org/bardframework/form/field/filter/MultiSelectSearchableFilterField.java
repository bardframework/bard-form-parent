package org.bardframework.form.field.filter;

import org.bardframework.form.field.FieldType;
import org.bardframework.form.model.SelectOption;

import java.util.List;

public class MultiSelectSearchableFilterField extends MultiSelectFilterField {

    public MultiSelectSearchableFilterField() {
    }

    public MultiSelectSearchableFilterField(String name, List<SelectOption> options) {
        super(name, options);
    }

    @Override
    public FieldType getType() {
        return FilterFieldType.MULTI_SELECT_SEARCHABLE_FILTER;
    }

}
