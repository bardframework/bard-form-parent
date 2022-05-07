package org.bardframework.form.common.field;

import org.bardframework.form.common.FieldType;
import org.bardframework.form.common.FieldTypeBase;

public class TextAreaField extends TextField {
    public TextAreaField() {
    }

    public TextAreaField(String name) {
        super(name);
    }

    @Override
    public FieldType<?> getType() {
        return FieldTypeBase.TEXT_AREA;
    }

}
