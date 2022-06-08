package org.bardframework.form.field.input;

import org.bardframework.form.model.SelectOption;

import java.util.List;

public class SingleSelectSearchableField extends SingleSelectField {
    public SingleSelectSearchableField() {
    }

    public SingleSelectSearchableField(String name, List<SelectOption> options) {
        super(name, options);
    }

    @Override
    public InputFieldType getType() {
        return InputFieldType.SINGLE_SELECT_SEARCHABLE;
    }

}
