package org.bardframework.form.field.view;

import org.bardframework.form.field.Field;

public class FileDownloadField extends Field {

    private String url;
    private String contentType;
    private Integer size;

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
}