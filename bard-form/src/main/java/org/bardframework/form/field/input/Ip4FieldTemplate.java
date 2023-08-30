package org.bardframework.form.field.input;

public class Ip4FieldTemplate extends IpFieldTemplate<Ip4Field> {

    private final static int IP4_LENGTH = 32;

    protected Ip4FieldTemplate(String name) {
        super(name);
    }

    @Override
    protected int getIpLength() {
        return IP4_LENGTH;
    }
}