package org.bardframework.form.common.field;

import org.bardframework.form.common.FieldType;
import org.bardframework.form.common.FieldTypeBase;
import org.bardframework.form.common.field.base.Field;

public class DividerField extends Field {

    @Override
    public FieldType<?> getType() {
        return FieldTypeBase.DIVIDER;
    }

}
