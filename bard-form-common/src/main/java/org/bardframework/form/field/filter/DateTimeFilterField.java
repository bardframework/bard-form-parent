package org.bardframework.form.field.filter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bardframework.form.field.FieldType;

import java.time.temporal.ChronoUnit;

@Getter
@Setter
@ToString
public class DateTimeFilterField extends LongFilterField {

    private ChronoUnit lengthUnit;

    public DateTimeFilterField() {
    }

    public DateTimeFilterField(String name) {
        super(name);
    }

    @Override
    public FieldType getType() {
        return FilterFieldType.DATE_TIME_FILTER;
    }
}