package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bardframework.form.field.FieldType;

@Getter
@Setter
@ToString
public class ImageUploadField extends FileUploadField {

    private Double aspectRatio;
    private Double aspectRatioTolerance;
    private Integer minWidth;
    private Integer maxWidth;
    private Integer minHeight;
    private Integer maxHeight;
    private Boolean cropper;

    public ImageUploadField() {
    }

    protected ImageUploadField(String name) {
        super(name);
    }

    @Override
    public FieldType getType() {
        return InputFieldType.IMAGE_UPLOAD;
    }
}