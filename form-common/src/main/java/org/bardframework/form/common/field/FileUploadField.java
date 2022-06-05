package org.bardframework.form.common.field;

import org.bardframework.form.common.FieldType;
import org.bardframework.form.common.FieldTypeBase;
import org.bardframework.form.common.field.base.FormField;

import java.util.List;

public class FileUploadField extends FormField<String> {

    private Integer minSize;
    private Integer maxSize;
    private List<String> contentTypes;
    private String uploadAction;
    private String downloadAction;

    public FileUploadField() {
    }

    protected FileUploadField(String name) {
        super(name);
    }

    @Override
    public FieldType<?> getType() {
        return FieldTypeBase.FILE_UPLOAD;
    }

    @Override
    public String toString(String value) {
        return value;
    }

    public Integer getMinSize() {
        return minSize;
    }

    public void setMinSize(Integer minSize) {
        this.minSize = minSize;
    }

    public Integer getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(Integer maxSize) {
        this.maxSize = maxSize;
    }

    public List<String> getContentTypes() {
        return contentTypes;
    }

    public void setContentTypes(List<String> contentTypes) {
        this.contentTypes = contentTypes;
    }

    public String getUploadAction() {
        return uploadAction;
    }

    public void setUploadAction(String uploadAction) {
        this.uploadAction = uploadAction;
    }

    public String getDownloadAction() {
        return downloadAction;
    }

    public void setDownloadAction(String downloadAction) {
        this.downloadAction = downloadAction;
    }

}