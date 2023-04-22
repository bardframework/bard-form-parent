package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.bardframework.form.field.FieldType;

import java.util.List;

@Slf4j
@Getter
@Setter
@ToString
public class FileUploadField extends InputField<String> {

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
    public FieldType getType() {
        return InputFieldType.FILE_UPLOAD;
    }

    @Override
    public String toString(String value) {
        return value;
    }
}