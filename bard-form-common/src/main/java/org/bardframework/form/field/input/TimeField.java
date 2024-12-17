package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bardframework.form.field.FieldType;

import java.time.LocalTime;

@Getter
@Setter
@ToString
public class TimeField extends InputField<LocalTime> {
    private LocalTime minValue;
    private LocalTime maxValue;

    public TimeField() {
    }

    public TimeField(String name) {
        super(name);
    }

    @Override
    public FieldType getType() {
        return InputFieldType.TIME;
    }

}