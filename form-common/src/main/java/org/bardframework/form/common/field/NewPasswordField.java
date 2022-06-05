package org.bardframework.form.common.field;

import com.fasterxml.jackson.annotation.JsonTypeName;
import org.bardframework.form.common.FieldType;
import org.bardframework.form.common.FieldTypeBase;

@JsonTypeName("NEW_PASSWORD")
public class NewPasswordField extends TextField {

    @Override
    public FieldType<?> getType() {
        return FieldTypeBase.NEW_PASSWORD;
    }
}