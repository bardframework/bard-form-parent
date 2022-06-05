package org.bardframework.form.field;

import org.bardframework.form.common.field.ImageField;

public class ImageFieldTemplate extends FileFieldTemplate {

    public ImageFieldTemplate(String name) {
        super(name);
    }

    @Override
    protected ImageField getEmptyField() {
        return new ImageField();
    }

}
