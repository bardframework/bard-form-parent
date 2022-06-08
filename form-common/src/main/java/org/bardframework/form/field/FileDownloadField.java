package org.bardframework.form.field;

import org.bardframework.form.common.Field;

public class FileDownloadField extends Field {

    private String url;
    private String contentType;
    private Integer size;

    @Override
    public FieldType getType() {
        return FieldType.FILE_DOWNLOAD;
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