package org.bardframework.form.field.view;

import lombok.Getter;
import lombok.Setter;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;
import org.bardframework.form.field.FieldTemplate;
import org.bardframework.form.field.value.ImageFieldDataProvider;

import java.util.Locale;
import java.util.Map;

@Getter
@Setter
public class ImageViewFieldTemplate extends FieldTemplate<ImageViewField> {

    protected ImageFieldDataProvider dataProvider;

    public ImageViewFieldTemplate(String name) {
        super(name);
    }

    @Override
    public void fill(FormTemplate formTemplate, ImageViewField field, Map<String, Object> values, Map<String, Object> args, Locale locale) throws Exception {
        super.fill(formTemplate, field, values, args, locale);
        field.setName(FormUtils.getFieldStringProperty(formTemplate, this, "name", locale, args, this.getDefaultValue().getName()));
        field.setFileName(FormUtils.getFieldStringProperty(formTemplate, this, "fileName", locale, args, this.getDefaultValue().getFileName()));
        field.setData(FormUtils.getFieldStringProperty(formTemplate, this, "data", locale, args, this.getDefaultValue().getData()));
        field.setContentType(FormUtils.getFieldStringProperty(formTemplate, this, "contentType", locale, args, this.getDefaultValue().getContentType()));
        field.setSize(FormUtils.getFieldIntegerProperty(formTemplate, this, "size", locale, args, this.getDefaultValue().getSize()));
        field.setDescription(FormUtils.getFieldStringProperty(formTemplate, this, "description", locale, args, this.getDefaultValue().getDescription()));
        field.setCopyableDescription(FormUtils.getFieldBooleanProperty(formTemplate, this, "copyableDescription", locale, args, this.getDefaultValue().getCopyableDescription()));
        field.setVisibleSeconds(FormUtils.getFieldIntegerProperty(formTemplate, this, "visibleSeconds", locale, args, this.getDefaultValue().getVisibleSeconds()));
        field.setSpoilSeconds(FormUtils.getFieldIntegerProperty(formTemplate, this, "spoilSeconds", locale, args, this.getDefaultValue().getSpoilSeconds()));
        field.setWidth(FormUtils.getFieldIntegerProperty(formTemplate, this, "width", locale, args, this.getDefaultValue().getWidth()));
        field.setCenter(FormUtils.getFieldBooleanProperty(formTemplate, this, "center", locale, args, this.getDefaultValue().getCenter()));
        if (null != dataProvider) {
            dataProvider.fillData(field, args);
        }
    }
}
