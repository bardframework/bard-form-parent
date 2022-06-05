package org.bardframework.form.common.field;

import com.fasterxml.jackson.annotation.JsonTypeName;
import org.bardframework.form.common.FieldType;
import org.bardframework.form.common.FieldTypeBase;

@JsonTypeName("IMAGE")
public class ImageField extends FileField {

    @Override
    public FieldType<?> getType() {
        return FieldTypeBase.IMAGE;
    }
}