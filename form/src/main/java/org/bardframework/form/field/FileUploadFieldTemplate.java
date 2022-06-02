package org.bardframework.form.field;

import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;
import org.bardframework.form.common.field.FileUploadField;
import org.bardframework.form.field.base.FormFieldTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.Map;

public class FileUploadFieldTemplate extends FormFieldTemplate<FileUploadField, String> {
    protected static final Logger LOGGER = LoggerFactory.getLogger(FileUploadFieldTemplate.class);

    private final String uploadAction;
    private final String downloadAction;

    public FileUploadFieldTemplate(String name, String uploadAction, String downloadAction) {
        super(name, true);
        this.uploadAction = uploadAction;
        this.downloadAction = downloadAction;
    }

    @Override
    public void fill(FormTemplate formTemplate, FileUploadField field, Map<String, String> args, Locale locale) throws Exception {
        super.fill(formTemplate, field, args, locale);
        field.setMinSize(FormUtils.getIntegerValue(formTemplate, this.getName(), "minSize", locale, args));
        field.setMaxSize(FormUtils.getIntegerValue(formTemplate, this.getName(), "maxSize", locale, args));
        field.setContentTypes(FormUtils.getListValue(formTemplate, this.getName(), "contentTypes", locale, args));
        field.setUploadAction(this.getUploadAction());
        field.setDownloadAction(this.getDownloadAction());
    }

    @Override
    public boolean isValid(FileUploadField field, String value) {
        //TODO not implement
        return true;
    }

    @Override
    public String toValue(String value) {
        return value;
    }

    public String getDownloadAction() {
        return downloadAction;
    }

    public String getUploadAction() {
        return uploadAction;
    }
}
