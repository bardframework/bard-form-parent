package org.bardframework.form.field.input;

import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;
import org.bardframework.form.field.FormFieldTemplate;

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
        field.setMinSize(FormUtils.getFieldIntegerProperty(formTemplate, this.getName(), "minSize", locale, args, null));
        field.setMaxSize(FormUtils.getFieldIntegerProperty(formTemplate, this.getName(), "maxSize", locale, args, null));
        field.setContentTypes(FormUtils.getFieldListProperty(formTemplate, this.getName(), "contentTypes", locale, args, null));
        field.setUploadAction(this.getUploadAction());
        field.setDownloadAction(this.getDownloadAction());

        field.setAspectRatio(FormUtils.getFieldDoubleProperty(formTemplate, this.getName(), "aspectRatio", locale, args, null));
        field.setAspectRatio(FormUtils.getFieldDoubleProperty(formTemplate, this.getName(), "aspectRatioTolerance", locale, args, null));
        field.setMinWidth(FormUtils.getFieldIntegerProperty(formTemplate, this.getName(), "minWidth", locale, args, null));
        field.setMaxWidth(FormUtils.getFieldIntegerProperty(formTemplate, this.getName(), "maxWidth", locale, args, null));
        field.setMinHeight(FormUtils.getFieldIntegerProperty(formTemplate, this.getName(), "minHeight", locale, args, null));
        field.setMaxHeight(FormUtils.getFieldIntegerProperty(formTemplate, this.getName(), "maxHeight", locale, args, null));
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
