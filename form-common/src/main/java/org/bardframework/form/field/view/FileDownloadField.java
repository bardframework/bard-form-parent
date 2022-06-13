package org.bardframework.form.field.view;

import org.bardframework.form.field.Field;

public class FileDownloadField extends Field {

    private String url;
    private String fileName;
    private String contentType;
    private Integer size;
    private String description;

    @Override
    public ViewFieldType getType() {
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}