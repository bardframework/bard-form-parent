package org.bardframework.form.field.filter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bardframework.form.field.FieldType;
import org.bardframework.form.field.input.InputField;
import org.bardframework.form.model.filter.LongFilter;

@Getter
@Setter
@ToString
public class LongFilterField extends InputField<LongFilter> {

    private Long minLength;
    private Long maxLength;
    private Long minValue;
    private Long maxValue;

    public LongFilterField() {
    }

    public LongFilterField(String name) {
        super(name);
    }

    @Override
    public FieldType getType() {
        return FilterFieldType.NUMBER_FILTER;
    }

}