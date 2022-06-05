package org.bardframework.form.field;

import org.bardframework.form.common.field.AvatarViewField;

public class AvatarFieldTemplate extends FileFieldTemplate {

    public AvatarFieldTemplate(String name) {
        super(name);
    }

    @Override
    protected AvatarViewField getEmptyField() {
        return new AvatarViewField();
    }
}
