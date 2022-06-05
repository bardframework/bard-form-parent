package org.bardframework.form.field;

import org.bardframework.form.common.field.AvatarField;

public class AvatarFieldTemplate extends FileFieldTemplate {

    public AvatarFieldTemplate(String name) {
        super(name);
    }

    @Override
    protected AvatarField getEmptyField() {
        return new AvatarField();
    }
}
