package org.bardframework.form.field.view;

import lombok.Getter;
import lombok.Setter;
import org.bardframework.form.field.FieldType;

@Getter
@Setter
public class ImageViewField extends FileDownloadField {

    private Integer visibleSeconds;
    private Integer spoilSeconds;
    private Boolean copyableDescription;

    @Override
    public FieldType getType() {
        return ViewFieldType.IMAGE_VIEW;
    }
}