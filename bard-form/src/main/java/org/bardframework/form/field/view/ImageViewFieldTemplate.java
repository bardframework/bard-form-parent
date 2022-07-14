package org.bardframework.form.field.view;

public class ImageViewFieldTemplate extends FileDownloadFieldTemplate {

    public ImageViewFieldTemplate(String name) {
        super(name);
    }

    @Override
    protected ImageViewField getEmptyField() {
        return new ImageViewField();
    }

}
