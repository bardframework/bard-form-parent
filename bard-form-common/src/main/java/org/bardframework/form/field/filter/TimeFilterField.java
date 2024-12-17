package org.bardframework.form.field.filter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bardframework.form.field.FieldType;
import org.bardframework.form.field.input.InputField;
import org.bardframework.form.model.filter.LocalTimeFilter;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@ToString
public class TimeFilterField extends InputField<LocalTimeFilter> {
    private Integer minLength;
    private Integer maxLength;
    private ChronoUnit lengthUnit;
    private LocalTime minValue;
    private LocalTime maxValue;

    public TimeFilterField() {
    }

    public TimeFilterField(String name) {
        super(name);
    }

    @Override
    public FieldType getType() {
        return FilterFieldType.TIME_FILTER;
    }

}
