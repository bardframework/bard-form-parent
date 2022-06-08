package org.bardframework.form.field;

public class AvatarFieldTemplate extends FileFieldTemplate {

    public AvatarFieldTemplate(String name) {
        super(name);
    }

    @Override
    protected AvatarViewField getEmptyField() {
        return new AvatarViewField();
    }
}
