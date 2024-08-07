package org.bardframework.form.field.filter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bardframework.form.field.FieldType;

@Getter
@Setter
@ToString
public class DateFilterField extends LongFilterField {

    public DateFilterField() {
    }

    public DateFilterField(String name) {
        super(name);
    }

    @Override
    public FieldType getType() {
        return FilterFieldType.DATE_FILTER;
    }
}