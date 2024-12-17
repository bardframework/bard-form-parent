package org.bardframework.form.field.view;

import lombok.Getter;
import lombok.Setter;
import org.bardframework.form.field.Field;
import org.bardframework.form.field.FieldType;

@Getter
@Setter
public class ImageViewField extends Field {

    private String data;
    private String fileName;
    private String contentType;
    private Integer size;
    private Integer visibleSeconds;
    private Integer spoilSeconds;
    private Boolean copyableDescription;
    private Integer width;
    private Boolean center;

    @Override
    public FieldType getType() {
        return ViewFieldType.IMAGE_VIEW;
    }
}