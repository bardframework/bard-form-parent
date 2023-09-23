package org.bardframework.form.field.filter;

import org.bardframework.form.field.input.Ip6FieldTemplate;

public class Ip6FilterFieldTemplate extends IpFilterFieldTemplate<Ip6FilterField> {

    protected Ip6FilterFieldTemplate(String name) {
        super(name);
    }

    @Override
    protected int getIpLength() {
        return Ip6FieldTemplate.IP6_LENGTH;
    }
}