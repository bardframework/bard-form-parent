package org.bardframework.form.field.view;

public class ImageViewField extends FileDownloadField {

    @Override
    public ViewFieldType getType() {
        return ViewFieldType.IMAGE_VIEW;
    }
}