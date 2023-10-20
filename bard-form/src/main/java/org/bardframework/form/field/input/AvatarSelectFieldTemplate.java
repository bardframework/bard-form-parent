package org.bardframework.form.field.input;

import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;

import java.util.Locale;
import java.util.Map;

public class AvatarSelectFieldTemplate extends InputFieldTemplate<AvatarSelectField, String> {
    private String uploadAction;
    private String downloadAction;

    public AvatarSelectFieldTemplate(String name, String uploadAction, String downloadAction) {
        this(name);
        this.uploadAction = uploadAction;
        this.downloadAction = downloadAction;
    }

    public AvatarSelectFieldTemplate(String name) {
        super(name);
    }

    @Override
    public void fill(FormTemplate formTemplate, AvatarSelectField field, Map<String, String> values, Locale locale) throws Exception {
        super.fill(formTemplate, field, values, locale);
        field.setMinSize(FormUtils.getFieldIntegerProperty(formTemplate, this, "minSize", locale, values, this.getDefaultValues().getMinSize()));
        field.setMaxSize(FormUtils.getFieldIntegerProperty(formTemplate, this, "maxSize", locale, values, this.getDefaultValues().getMaxSize()));
        field.setContentTypes(FormUtils.getFieldListProperty(formTemplate, this, "contentTypes", locale, values, this.getDefaultValues().getContentTypes()));
        field.setUploadAction(this.getUploadAction());
        field.setDownloadAction(this.getDownloadAction());
        field.setAspectRatio(FormUtils.getFieldDoubleProperty(formTemplate, this, "aspectRatio", locale, values, this.getDefaultValues().getAspectRatio()));
        field.setAspectRatio(FormUtils.getFieldDoubleProperty(formTemplate, this, "aspectRatioTolerance", locale, values, this.getDefaultValues().getAspectRatioTolerance()));
        field.setMinWidth(FormUtils.getFieldIntegerProperty(formTemplate, this, "minWidth", locale, values, this.getDefaultValues().getMinWidth()));
        field.setMaxWidth(FormUtils.getFieldIntegerProperty(formTemplate, this, "maxWidth", locale, values, this.getDefaultValues().getMaxWidth()));
        field.setMinHeight(FormUtils.getFieldIntegerProperty(formTemplate, this, "minHeight", locale, values, this.getDefaultValues().getMinHeight()));
        field.setMaxHeight(FormUtils.getFieldIntegerProperty(formTemplate, this, "maxHeight", locale, values, this.getDefaultValues().getMaxHeight()));
        field.setCropper(true);
    }

    @Override
    public boolean isValid(String flowToken, AvatarSelectField field, String value, Map<String, String> flowData) {
        return true;
    }

    @Override
    public String toValue(String value) {
        return value;
    }

    public String getUploadAction() {
        return this.uploadAction;
    }

    public String getDownloadAction() {
        return this.downloadAction;
    }
}
