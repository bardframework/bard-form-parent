package org.bardframework.form.field.view;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AvatarViewDownloadFieldTemplate extends ImageViewFieldTemplate {

    public AvatarViewDownloadFieldTemplate(String name) {
        super(name);
    }

    @Override
    protected AvatarViewField getEmptyField() {
        return new AvatarViewField();
    }
}
