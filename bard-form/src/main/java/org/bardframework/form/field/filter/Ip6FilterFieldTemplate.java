package org.bardframework.form.field.filter;

import lombok.Getter;
import lombok.Setter;
import org.bardframework.form.field.input.Ip6FieldTemplate;

@Getter
@Setter
public class Ip6FilterFieldTemplate extends IpFilterFieldTemplateAbstract<Ip6FilterField> {

    public Ip6FilterFieldTemplate(String name) {
        super(name);
    }

    @Override
    protected int getIpLength() {
        return Ip6FieldTemplate.IP6_LENGTH;
    }
}