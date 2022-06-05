package org.bardframework.form.common.field;

import org.bardframework.form.common.FieldType;
import org.bardframework.form.common.FieldTypeBase;
import org.bardframework.form.common.field.base.FormField;

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
    public FieldType<?> getType() {
        return FieldTypeBase.SINGLE_CHECKBOX;
    }

    @Override
    public String toString(Boolean value) {
        return null == value ? null : value.toString();
    }
}
