package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bardframework.form.field.FieldType;

@Getter
@Setter
@ToString
public class Ip4Field extends InputField<String> {

    private String minValue;
    private String maxValue;

    public Ip4Field() {
    }

    public Ip4Field(String name) {
        super(name);
    }

    @Override
    public FieldType getType() {
        return InputFieldType.IP4;
    }

}