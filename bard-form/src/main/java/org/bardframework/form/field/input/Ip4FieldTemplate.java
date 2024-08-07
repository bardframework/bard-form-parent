package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Ip4FieldTemplate extends IpFieldTemplateAbstract<Ip4Field> {

    public static final int IP4_LENGTH = 32;

    public Ip4FieldTemplate(String name) {
        super(name);
    }

    @Override
    protected int getIpLength() {
        return IP4_LENGTH;
    }
}