package org.bardframework.form.field.filter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bardframework.form.field.FieldType;

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
