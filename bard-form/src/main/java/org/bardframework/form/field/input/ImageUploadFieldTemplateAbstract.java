package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@Getter
@Setter
abstract class ImageUploadFieldTemplateAbstract<F extends ImageUploadField> extends FileUploadFieldTemplateAbstract<F> {

    public ImageUploadFieldTemplateAbstract(String name) {
        super(name, true);
    }

    @Override
    public void fill(FormTemplate formTemplate, F field, Map<String, Object> values, Map<String, Object> args, Locale locale) throws Exception {
        super.fill(formTemplate, field, values, args, locale);
        field.setAspectRatio(FormUtils.getFieldDoubleProperty(formTemplate, this, "aspectRatio", locale, args, this.getDefaultValue().getAspectRatio()));
        field.setAspectRatio(FormUtils.getFieldDoubleProperty(formTemplate, this, "aspectRatioTolerance", locale, args, this.getDefaultValue().getAspectRatioTolerance()));
        field.setMinWidth(FormUtils.getFieldIntegerProperty(formTemplate, this, "minWidth", locale, args, this.getDefaultValue().getMinWidth()));
        field.setMaxWidth(FormUtils.getFieldIntegerProperty(formTemplate, this, "maxWidth", locale, args, this.getDefaultValue().getMaxWidth()));
        field.setMinHeight(FormUtils.getFieldIntegerProperty(formTemplate, this, "minHeight", locale, args, this.getDefaultValue().getMinHeight()));
        field.setMaxHeight(FormUtils.getFieldIntegerProperty(formTemplate, this, "maxHeight", locale, args, this.getDefaultValue().getMaxHeight()));
        field.setCropper(FormUtils.getFieldBooleanProperty(formTemplate, this, "cropper", locale, args, this.getDefaultValue().getCropper()));
    }

    @Override
    public boolean isValid(String flowToken, F field, List<String> values, Map<String, Object> flowData) throws Exception {
        //TODO not implement
        return super.isValid(flowToken, field, values, flowData);
    }
}
