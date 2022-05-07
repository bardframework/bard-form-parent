package org.bardframework.form.common.field;

import org.bardframework.form.common.FieldType;
import org.bardframework.form.common.FieldTypeBase;

public class ImageField extends FileField {

    @Override
    public FieldType<?> getType() {
        return FieldTypeBase.IMAGE;
    }
}