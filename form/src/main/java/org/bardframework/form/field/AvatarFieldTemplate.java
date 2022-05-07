package org.bardframework.form.field;

import org.bardframework.form.common.field.AvatarField;
import org.bardframework.form.common.field.FileField;

public class AvatarFieldTemplate extends FileFieldTemplate {

    public AvatarFieldTemplate(String name) {
        super(name);
    }

    @Override
    public FileField getEmptyField() {
        return new AvatarField();
    }
}
