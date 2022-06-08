package org.bardframework.form.field.input;

import org.bardframework.form.model.SelectOption;

import java.util.List;

public class MultiSelectSearchableField extends MultiSelectField {

    public MultiSelectSearchableField() {
    }

    public MultiSelectSearchableField(String name, List<SelectOption> options) {
        super(name, options);
    }

    @Override
    public InputFieldType getType() {
        return InputFieldType.MULTI_SELECT_SEARCHABLE;
    }

}
