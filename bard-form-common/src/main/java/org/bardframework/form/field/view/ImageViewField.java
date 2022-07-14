package org.bardframework.form.field.view;

import org.bardframework.form.field.FieldType;

public class ImageViewField extends FileDownloadField {

    @Override
    public FieldType getType() {
        return ViewFieldType.IMAGE_VIEW;
    }
}