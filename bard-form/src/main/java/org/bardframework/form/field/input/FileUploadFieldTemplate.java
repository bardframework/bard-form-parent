package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class FileUploadFieldTemplate extends FileUploadFieldTemplateAbstract<FileUploadField> {

    public FileUploadFieldTemplate(String name) {
        super(name, true);
    }

    @Override
    public boolean isValid(String flowToken, FileUploadField field, List<String> values, Map<String, String> flowData) {
        //TODO not implement
        return true;
    }
}
