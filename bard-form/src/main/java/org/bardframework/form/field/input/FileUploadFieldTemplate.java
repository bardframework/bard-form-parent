package org.bardframework.form.field.input;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;

import java.util.Locale;
import java.util.Map;

@Slf4j
@Getter
public class FileUploadFieldTemplate extends InputFieldTemplate<FileUploadField, String> {

    private final String uploadAction;
    private final String downloadAction;

    public FileUploadFieldTemplate(String name, String uploadAction, String downloadAction) {
        super(name, true);
        this.uploadAction = uploadAction;
        this.downloadAction = downloadAction;
    }

    @Override
    public void fill(FormTemplate formTemplate, FileUploadField field, Map<String, String> values, Locale locale, HttpServletRequest httpRequest) throws Exception {
        super.fill(formTemplate, field, values, locale, httpRequest);
        field.setMinSize(FormUtils.getFieldIntegerProperty(formTemplate, this, "minSize", locale, values, this.getDefaultValues().getMinSize()));
        field.setMaxSize(FormUtils.getFieldIntegerProperty(formTemplate, this, "maxSize", locale, values, this.getDefaultValues().getMaxSize()));
        field.setContentTypes(FormUtils.getFieldListProperty(formTemplate, this, "contentTypes", locale, values, this.getDefaultValues().getContentTypes()));
        field.setUploadAction(this.getUploadAction());
        field.setDownloadAction(this.getDownloadAction());
    }

    @Override
    public boolean isValid(String flowToken, FileUploadField field, String value, Map<String, String> flowData) {
        //TODO not implement
        return true;
    }

    @Override
    public String toValue(String value) {
        return value;
    }
}
