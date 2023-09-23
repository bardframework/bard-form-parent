package org.bardframework.form.field.filter;

import org.bardframework.form.field.input.Ip4FieldTemplate;

public class Ip4FilterFieldTemplate extends IpFilterFieldTemplate<Ip4FilterField> {

    protected Ip4FilterFieldTemplate(String name) {
        super(name);
    }

    @Override
    protected int getIpLength() {
        return Ip4FieldTemplate.IP4_LENGTH;
    }
}