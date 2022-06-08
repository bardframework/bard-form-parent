package org.bardframework.form.field;

public class ImageViewField extends FileDownloadField {

    @Override
    public FieldType getType() {
        return FieldType.IMAGE_VIEW;
    }
}