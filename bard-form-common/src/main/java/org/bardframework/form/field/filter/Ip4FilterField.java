package org.bardframework.form.field.filter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.bardframework.form.field.FieldType;
import org.bardframework.form.field.input.InputField;
import org.bardframework.form.model.filter.LocalTimeFilter;

@Slf4j
@Getter
@Setter
@ToString
public class Ip4FilterField extends InputField<LocalTimeFilter> {
    private String minValue;
    private String maxValue;

    public Ip4FilterField() {
    }

    public Ip4FilterField(String name) {
        super(name);
    }

    @Override
    public FieldType getType() {
        return FilterFieldType.IP4_FILTER;
    }

    @Override
    public String toString(LocalTimeFilter value) {
        return String.join(SEPARATOR, null == value.getFrom() ? "" : value.getFrom().toString(), null == value.getTo() ? "" : value.getTo().toString());
    }
}
