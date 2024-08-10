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
    public void fill(FormTemplate formTemplate, F field, Map<String, String> values, Locale locale) throws Exception {
        super.fill(formTemplate, field, values, locale);
        field.setAspectRatio(FormUtils.getFieldDoubleProperty(formTemplate, this, "aspectRatio", locale, values, this.getDefaultValue().getAspectRatio()));
        field.setAspectRatio(FormUtils.getFieldDoubleProperty(formTemplate, this, "aspectRatioTolerance", locale, values, this.getDefaultValue().getAspectRatioTolerance()));
        field.setMinWidth(FormUtils.getFieldIntegerProperty(formTemplate, this, "minWidth", locale, values, this.getDefaultValue().getMinWidth()));
        field.setMaxWidth(FormUtils.getFieldIntegerProperty(formTemplate, this, "maxWidth", locale, values, this.getDefaultValue().getMaxWidth()));
        field.setMinHeight(FormUtils.getFieldIntegerProperty(formTemplate, this, "minHeight", locale, values, this.getDefaultValue().getMinHeight()));
        field.setMaxHeight(FormUtils.getFieldIntegerProperty(formTemplate, this, "maxHeight", locale, values, this.getDefaultValue().getMaxHeight()));
    }

    @Override
    public boolean isValid(String flowToken, F field, List<String> value, Map<String, String> flowData) throws Exception {
        //TODO not implement
        return true;
    }
}
