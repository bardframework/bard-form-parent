package org.bardframework.form.field.filter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.bardframework.form.field.FieldType;
import org.bardframework.form.field.input.InputField;
import org.bardframework.form.model.filter.LongFilter;

@Slf4j
@Getter
@Setter
@ToString
public class NumberFilterField extends InputField<LongFilter> {
    private Long minValue;
    private Long maxValue;
    private Long minLength;
    private Long maxLength;

    public NumberFilterField() {
    }

    protected NumberFilterField(String name) {
        super(name);
    }

    @Override
    public FieldType getType() {
        return FilterFieldType.NUMBER_FILTER;
    }

    @Override
    public String toString(LongFilter value) {
        return String.join(SEPARATOR, null == value.getFrom() ? "" : value.getFrom().toString(), null == value.getTo() ? "" : value.getTo().toString());
    }
}