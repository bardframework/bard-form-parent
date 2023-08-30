package org.bardframework.form.field.input;

public class Ip6FieldTemplate extends IpFieldTemplate<Ip6Field> {

    private final static int IP6_LENGTH = 12;

    protected Ip6FieldTemplate(String name) {
        super(name);
    }

    @Override
    protected int getIpLength() {
        return IP6_LENGTH;
    }
}