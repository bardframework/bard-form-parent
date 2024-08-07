package org.bardframework.form.field.filter;

import lombok.Getter;
import lombok.Setter;
import org.bardframework.form.field.input.Ip4FieldTemplate;

@Getter
@Setter
public class Ip4FilterFieldTemplate extends IpFilterFieldTemplateAbstract<Ip4FilterField> {

    public Ip4FilterFieldTemplate(String name) {
        super(name);
    }

    @Override
    protected int getIpLength() {
        return Ip4FieldTemplate.IP4_LENGTH;
    }
}