package org.bardframework.form.field.view;

import org.bardframework.form.field.FieldType;

public class AvatarViewField extends ImageViewField {

    @Override
    public FieldType getType() {
        return ViewFieldType.AVATAR_VIEW;
    }
}