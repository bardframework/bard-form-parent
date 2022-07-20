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
        field.setMinSize(FormUtils.getFieldIntegerProperty(formTemplate, this, "minSize", locale, args, this.getDefaultValues().getMinSize()));
        field.setMaxSize(FormUtils.getFieldIntegerProperty(formTemplate, this, "maxSize", locale, args, this.getDefaultValues().getMaxSize()));
        field.setContentTypes(FormUtils.getFieldListProperty(formTemplate, this, "contentTypes", locale, args, this.getDefaultValues().getContentTypes()));
        field.setUploadAction(this.getUploadAction());
        field.setDownloadAction(this.getDownloadAction());

        field.setAspectRatio(FormUtils.getFieldDoubleProperty(formTemplate, this, "aspectRatio", locale, args, this.getDefaultValues().getAspectRatio()));
        field.setAspectRatio(FormUtils.getFieldDoubleProperty(formTemplate, this, "aspectRatioTolerance", locale, args, this.getDefaultValues().getAspectRatioTolerance()));
        field.setMinWidth(FormUtils.getFieldIntegerProperty(formTemplate, this, "minWidth", locale, args, this.getDefaultValues().getMinWidth()));
        field.setMaxWidth(FormUtils.getFieldIntegerProperty(formTemplate, this, "maxWidth", locale, args, this.getDefaultValues().getMaxWidth()));
        field.setMinHeight(FormUtils.getFieldIntegerProperty(formTemplate, this, "minHeight", locale, args, this.getDefaultValues().getMinHeight()));
        field.setMaxHeight(FormUtils.getFieldIntegerProperty(formTemplate, this, "maxHeight", locale, args, this.getDefaultValues().getMaxHeight()));
    }

    @Override
    public boolean isValid(ImageUploadField field, String value, Map<String, String> args) {
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
