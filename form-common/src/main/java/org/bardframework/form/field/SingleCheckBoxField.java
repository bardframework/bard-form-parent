package org.bardframework.form.field;

import org.bardframework.form.common.FormField;

public class SingleCheckBoxField extends FormField<Boolean> {

    public SingleCheckBoxField() {
    }

    public SingleCheckBoxField(String name) {
        super(name);
    }

    public Boolean toValue(String value) {
        return Boolean.valueOf(value);
    }

    @Override
    public FieldType getType() {
        return FieldType.SINGLE_CHECKBOX;
    }

    @Override
    public String toString(Boolean value) {
        return null == value ? null : value.toString();
    }
}
