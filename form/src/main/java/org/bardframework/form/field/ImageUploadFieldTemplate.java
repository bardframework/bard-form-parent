package org.bardframework.form.field;

import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;
import org.bardframework.form.common.field.ImageUploadField;
import org.bardframework.form.field.base.FormFieldTemplate;

import java.util.Locale;
import java.util.Map;

//TODO inherit from FileUploadFieldTemplate
public class ImageUploadFieldTemplate extends FormFieldTemplate<ImageUploadField, String> {
    private final String uploadAction;
    private final String downloadAction;

    public ImageUploadFieldTemplate(String name, String uploadAction, String downloadAction) {
        super(name, true);
        this.uploadAction = uploadAction;
        this.downloadAction = downloadAction;
    }

    @Override
    public void fill(FormTemplate formTemplate, ImageUploadField field, Map<String, String> args, Locale locale) throws Exception {
        super.fill(formTemplate, field, args, locale);
        field.setMinSize(FormUtils.getIntegerValue(formTemplate, this.getName(), "minSize", locale, args));
        field.setMaxSize(FormUtils.getIntegerValue(formTemplate, this.getName(), "maxSize", locale, args));
        field.setContentTypes(FormUtils.getListValue(formTemplate, this.getName(), "contentTypes", locale, args));
        field.setUploadAction(this.getUploadAction());
        field.setDownloadAction(this.getDownloadAction());

        field.setAspectRatio(FormUtils.getDoubleValue(formTemplate, this.getName(), "aspectRatio", locale, args));
        field.setAspectRatio(FormUtils.getDoubleValue(formTemplate, this.getName(), "aspectRatioTolerance", locale, args));
        field.setMinWidth(FormUtils.getIntegerValue(formTemplate, this.getName(), "minWidth", locale, args));
        field.setMaxWidth(FormUtils.getIntegerValue(formTemplate, this.getName(), "maxWidth", locale, args));
        field.setMinHeight(FormUtils.getIntegerValue(formTemplate, this.getName(), "minHeight", locale, args));
        field.setMaxHeight(FormUtils.getIntegerValue(formTemplate, this.getName(), "maxHeight", locale, args));
    }

    @Override
    public boolean isValid(ImageUploadField field, String value) {
        //TODO not implement
        return true;
    }

    @Override
    public String toValue(String value) {
        return value;
    }

    public String getUploadAction() {
        return uploadAction;
    }

    public String getDownloadAction() {
        return downloadAction;
    }
}
