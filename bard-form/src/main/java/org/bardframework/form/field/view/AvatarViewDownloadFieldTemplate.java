package org.bardframework.form.field.view;

public class AvatarViewDownloadFieldTemplate extends FileDownloadFieldTemplate {

    public AvatarViewDownloadFieldTemplate(String name) {
        super(name);
    }

    @Override
    protected AvatarViewField getEmptyField() {
        return new AvatarViewField();
    }
}
