package org.bardframework.form.field.input;

import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Map;

//TODO inherit from FileUploadFieldTemplate
public class ImageUploadFieldTemplate extends InputFieldTemplate<ImageUploadField, String> {
    private final String uploadAction;
    private final String downloadAction;

    public ImageUploadFieldTemplate(String name, String uploadAction, String downloadAction) {
        super(name, true);
        this.uploadAction = uploadAction;
        this.downloadAction = downloadAction;
    }

    @Override
    public void fill(FormTemplate formTemplate, ImageUploadField field, Map<String, String> args, Locale locale, HttpServletRequest httpRequest) throws Exception {
        super.fill(formTemplate, field, args, locale, httpRequest);
        field.setMinSize(FormUtils.getFieldIntegerProperty(formTemplate, this, "minSize", locale, args, null));
        field.setMaxSize(FormUtils.getFieldIntegerProperty(formTemplate, this, "maxSize", locale, args, null));
        field.setContentTypes(FormUtils.getFieldListProperty(formTemplate, this, "contentTypes", locale, args, null));
        field.setUploadAction(this.getUploadAction());
        field.setDownloadAction(this.getDownloadAction());

        field.setAspectRatio(FormUtils.getFieldDoubleProperty(formTemplate, this, "aspectRatio", locale, args, null));
        field.setAspectRatio(FormUtils.getFieldDoubleProperty(formTemplate, this, "aspectRatioTolerance", locale, args, null));
        field.setMinWidth(FormUtils.getFieldIntegerProperty(formTemplate, this, "minWidth", locale, args, null));
        field.setMaxWidth(FormUtils.getFieldIntegerProperty(formTemplate, this, "maxWidth", locale, args, null));
        field.setMinHeight(FormUtils.getFieldIntegerProperty(formTemplate, this, "minHeight", locale, args, null));
        field.setMaxHeight(FormUtils.getFieldIntegerProperty(formTemplate, this, "maxHeight", locale, args, null));
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
