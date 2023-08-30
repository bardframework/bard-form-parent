package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.bardframework.form.field.FieldType;

@Slf4j
@Getter
@Setter
@ToString
public class Ip4Field extends InputField<String> {

    private String minValue;
    private String maxValue;

    public Ip4Field() {
    }

    protected Ip4Field(String name) {
        super(name);
    }

    @Override
    public FieldType getType() {
        return InputFieldType.IP4;
    }

    @Override
    public String toString(String value) {
        return value;
    }
}