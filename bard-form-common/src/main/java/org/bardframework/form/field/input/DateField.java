package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bardframework.commons.utils.DateTimeUtils;
import org.bardframework.form.field.FieldType;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class DateField extends InputField<LocalDate> {
    private LocalDate minValue;
    private LocalDate maxValue;

    public DateField() {
    }

    public DateField(String name) {
        super(name);
    }

    @Override
    public FieldType getType() {
        return InputFieldType.DATE;
    }

    @Override
    public String toString(LocalDate value) {
        return null == value ? null : value.toString();
    }
}