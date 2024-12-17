package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bardframework.form.field.FieldType;

import java.util.List;

@Getter
@Setter
@ToString
public class FileUploadField extends InputField<List<String>> {

    protected Integer minSize;
    protected Integer maxSize;
    protected Integer minTotalSize;
    protected Integer maxTotalSize;
    protected Byte minCount;
    protected Byte maxCount;
    protected List<String> validContentTypes;
    protected String uploadAction;
    protected String downloadAction;

    public FileUploadField() {
    }

    public FileUploadField(String name) {
        super(name);
    }

    @Override
    public FieldType getType() {
        return InputFieldType.FILE_UPLOAD;
    }

}