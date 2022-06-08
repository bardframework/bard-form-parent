package org.bardframework.form.field;

import org.bardframework.form.common.SelectOption;

import java.util.List;

public class MultiSelectSearchableField extends MultiSelectField {

    public MultiSelectSearchableField() {
    }

    public MultiSelectSearchableField(String name, List<SelectOption> options) {
        super(name, options);
    }

    @Override
    public FieldType getType() {
        return FieldType.MULTI_SELECT_SEARCHABLE;
    }

}
