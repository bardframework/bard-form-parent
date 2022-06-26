package org.bardframework.form.field.input;

import org.bardframework.form.field.FieldType;

public class ImageUploadField extends FileUploadField {

    private Double aspectRatio;
    private Double aspectRatioTolerance;
    private Integer minWidth;
    private Integer maxWidth;
    private Integer minHeight;
    private Integer maxHeight;

    public ImageUploadField() {
    }

    protected ImageUploadField(String name) {
        super(name);
    }

    @Override
    public FieldType getType() {
        return InputFieldType.IMAGE_UPLOAD;
    }

    public Double getAspectRatioTolerance() {
        return aspectRatioTolerance;
    }

    public void setAspectRatioTolerance(Double aspectRatioTolerance) {
        this.aspectRatioTolerance = aspectRatioTolerance;
    }

    public Double getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(Double aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public Integer getMinWidth() {
        return minWidth;
    }

    public void setMinWidth(Integer minWidth) {
        this.minWidth = minWidth;
    }

    public Integer getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(Integer maxWidth) {
        this.maxWidth = maxWidth;
    }

    public Integer getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(Integer minHeight) {
        this.minHeight = minHeight;
    }

    public Integer getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(Integer maxHeight) {
        this.maxHeight = maxHeight;
    }
}