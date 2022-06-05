package org.bardframework.form.field;

import org.bardframework.form.common.field.ImageViewField;

public class ImageFieldTemplate extends FileFieldTemplate {

    public ImageFieldTemplate(String name) {
        super(name);
    }

    @Override
    protected ImageViewField getEmptyField() {
        return new ImageViewField();
    }

}
