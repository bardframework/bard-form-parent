package org.bardframework.form.field.view;

import org.bardframework.form.field.Field;
import org.bardframework.form.field.FieldType;

public class FileDownloadField extends Field {

    private String src;
    private String fileName;
    private String contentType;
    private Integer size;

    @Override
    public FieldType getType() {
        return ViewFieldType.FILE_DOWNLOAD;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}