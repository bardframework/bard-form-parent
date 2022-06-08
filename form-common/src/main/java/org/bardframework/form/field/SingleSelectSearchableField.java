package org.bardframework.form.field;

import org.bardframework.form.common.SelectOption;

import java.util.List;

public class SingleSelectSearchableField extends SingleSelectField {
    public SingleSelectSearchableField() {
    }

    public SingleSelectSearchableField(String name, List<SelectOption> options) {
        super(name, options);
    }

    @Override
    public FieldType getType() {
        return FieldType.SINGLE_SELECT_SEARCHABLE;
    }

}
