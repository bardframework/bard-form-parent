package org.bardframework.form.field.filter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.bardframework.form.field.FieldType;
import org.bardframework.form.field.input.InputField;
import org.bardframework.form.model.SelectOption;
import org.bardframework.form.model.filter.IdFilter;

import java.util.List;

@Slf4j
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
    public String toString(IdFilter<String> value) {
        return null == value ? null : value.getEquals();
    }

    @Override
    public FieldType getType() {
        return FilterFieldType.SINGLE_SELECT_FILTER;
    }
}
