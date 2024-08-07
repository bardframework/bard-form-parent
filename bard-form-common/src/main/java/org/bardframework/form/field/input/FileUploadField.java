package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bardframework.form.field.FieldType;
import org.bardframework.form.model.StringListSubmitType;

import java.util.List;

@Getter
@Setter
@ToString
public class FileUploadField extends InputField<List<String>> {

    protected Integer minSize;
    protected Integer maxSize;
    protected Integer totalSize;
    protected List<String> contentTypes;
    protected String uploadAction;
    protected String downloadAction;
    protected Byte count;
    protected StringListSubmitType submitType;

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
    public String toString(List<String> value) {
        return null == value ? null : String.join(SEPARATOR, value);
    }
}