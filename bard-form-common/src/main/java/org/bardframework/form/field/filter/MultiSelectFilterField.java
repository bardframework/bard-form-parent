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
public class MultiSelectFilterField extends InputField<IdFilter<String>> {
    private Integer maxCount;
    private List<SelectOption> options;

    public MultiSelectFilterField() {
    }

    public MultiSelectFilterField(String name, List<SelectOption> options) {
        super(name);
        this.options = options;
    }

    @Override
    public FieldType getType() {
        return FilterFieldType.MULTI_SELECT_FILTER;
    }

    @Override
    public String toString(IdFilter<String> value) {
        return null == value ? null : String.join(SEPARATOR, value.getIn());
    }
}
