package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bardframework.form.field.FieldType;

@Getter
@Setter
@ToString
public class DateField extends InputField<Long> {
    private Long minValue;
    private Long maxValue;

    public DateField() {
    }

    public DateField(String name) {
        super(name);
    }

    @Override
    public FieldType getType() {
        return InputFieldType.DATE;
    }

}