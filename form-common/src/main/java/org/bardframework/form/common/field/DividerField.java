package org.bardframework.form.common.field;

import com.fasterxml.jackson.annotation.JsonTypeName;
import org.bardframework.form.common.FieldType;
import org.bardframework.form.common.FieldTypeBase;
import org.bardframework.form.common.field.base.Field;

@JsonTypeName("DIVIDER")
public class DividerField extends Field {

    @Override
    public FieldType<?> getType() {
        return FieldTypeBase.DIVIDER;
    }

}
