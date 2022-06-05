package org.bardframework.form.common.field;

import org.bardframework.form.common.FieldType;
import org.bardframework.form.common.FieldTypeBase;
import org.bardframework.form.common.field.common.SelectOption;

import java.util.List;

public class SingleSelectSearchableField extends SingleSelectField {
    public SingleSelectSearchableField() {
    }

    public SingleSelectSearchableField(String name, List<SelectOption> options) {
        super(name, options);
    }

    @Override
    public FieldType<?> getType() {
        return FieldTypeBase.SINGLE_SELECT_SEARCHABLE;
    }

}
