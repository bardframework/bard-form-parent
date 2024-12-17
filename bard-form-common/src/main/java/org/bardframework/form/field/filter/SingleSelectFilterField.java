package org.bardframework.form.field.filter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bardframework.form.field.FieldType;
import org.bardframework.form.field.input.InputField;
import org.bardframework.form.model.SelectOption;
import org.bardframework.form.model.filter.IdFilter;

import java.util.List;

@Getter
@Setter
@ToString
public class SingleSelectFilterField extends InputField<IdFilter<String>> {
    private List<SelectOption> options;

    public SingleSelectFilterField() {
    }

    public SingleSelectFilterField(String name, List<SelectOption> options) {
        super(name);
        this.options = options;
    }

    @Override
    public FieldType getType() {
        return FilterFieldType.SINGLE_SELECT_FILTER;
    }
}
