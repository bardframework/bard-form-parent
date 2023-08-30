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
public class Ip6Field extends Ip4Field {

    public Ip6Field() {
    }

    protected Ip6Field(String name) {
        super(name);
    }

    @Override
    public FieldType getType() {
        return InputFieldType.IP6;
    }
}