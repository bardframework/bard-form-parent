package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Ip6FieldTemplate extends IpFieldTemplateAbstract<Ip6Field> {

    public static final int IP6_LENGTH = 12;

    public Ip6FieldTemplate(String name) {
        super(name);
    }

    @Override
    protected int getIpLength() {
        return IP6_LENGTH;
    }
}