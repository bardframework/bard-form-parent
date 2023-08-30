package org.bardframework.form.field.filter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.bardframework.form.field.FieldType;

@Slf4j
@Getter
@Setter
@ToString
public class Ip6FilterField extends Ip4FilterField {
    public Ip6FilterField() {
    }

    public Ip6FilterField(String name) {
        super(name);
    }

    @Override
    public FieldType getType() {
        return FilterFieldType.IP6_FILTER;
    }
}
