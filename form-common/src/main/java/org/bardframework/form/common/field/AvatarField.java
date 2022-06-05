package org.bardframework.form.common.field;

import com.fasterxml.jackson.annotation.JsonTypeName;
import org.bardframework.form.common.FieldType;
import org.bardframework.form.common.FieldTypeBase;

@JsonTypeName("AVATAR")
public class AvatarField extends FileField {

    @Override
    public FieldType<?> getType() {
        return FieldTypeBase.AVATAR;
    }
}