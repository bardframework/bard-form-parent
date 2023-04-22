package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.bardframework.form.field.FieldType;
import org.bardframework.form.model.SelectOption;

import java.util.List;

@Slf4j
@Getter
@Setter
@ToString
public class SingleSelectSearchableField extends SingleSelectField {
    public SingleSelectSearchableField() {
    }

    public SingleSelectSearchableField(String name, List<SelectOption> options) {
        super(name, options);
    }

    @Override
    public FieldType getType() {
        return InputFieldType.SINGLE_SELECT_SEARCHABLE;
    }

}
