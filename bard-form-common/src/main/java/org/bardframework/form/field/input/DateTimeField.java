package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bardframework.commons.utils.DateTimeUtils;
import org.bardframework.form.field.FieldType;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class DateTimeField extends InputField<LocalDateTime> {
    private LocalDateTime minValue;
    private LocalDateTime maxValue;

    public DateTimeField() {
    }

    public DateTimeField(String name) {
        super(name);
    }

    @Override
    public FieldType getType() {
        return InputFieldType.DATE_TIME;
    }

    @Override
    public String toString(LocalDateTime value) {
        return null == value ? null : value.toString();
    }
}