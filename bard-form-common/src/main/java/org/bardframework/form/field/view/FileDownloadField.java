package org.bardframework.form.field.view;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bardframework.form.field.Field;
import org.bardframework.form.field.FieldType;

import java.util.List;

@Getter
@Setter
@ToString
public class FileDownloadField extends Field {

    private List<FileModel> files;

    @Override
    public FieldType getType() {
        return ViewFieldType.FILE_DOWNLOAD;
    }

    @Getter
    @Setter
    @ToString
    public static class FileModel {
        private String fileName;
        private String data;
        private String contentType;
        private Integer size;
        private String description;
    }
}