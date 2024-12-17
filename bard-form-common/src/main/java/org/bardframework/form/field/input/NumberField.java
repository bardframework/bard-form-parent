package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bardframework.form.field.FieldType;

@Getter
@Setter
@ToString
public class NumberField extends InputField<Long> {
    private Long minValue;
    private Long maxValue;
    private String mask;

    public NumberField() {
    }

    public NumberField(String name) {
        super(name);
    }

    @Override
    public FieldType getType() {
        return InputFieldType.NUMBER;
    }

}