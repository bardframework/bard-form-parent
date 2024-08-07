package org.bardframework.form.field.view;

import lombok.Getter;
import lombok.Setter;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;
import org.bardframework.form.field.FieldTemplate;
import org.bardframework.form.field.value.FileFieldDataProvider;

import java.util.Locale;
import java.util.Map;

@Getter
@Setter
public class ImageViewFieldTemplate extends FieldTemplate<ImageViewField> {

    protected FileFieldDataProvider dataProvider;

    public ImageViewFieldTemplate(String name) {
        super(name);
    }

    @Override
    public void fill(FormTemplate formTemplate, ImageViewField field, Map<String, String> args, Locale locale) throws Exception {
        super.fill(formTemplate, field, args, locale);
        field.setSrc(FormUtils.getFieldStringProperty(formTemplate, this, "src", locale, args, this.getDefaultValue().getSrc()));
        field.setFileName(FormUtils.getFieldStringProperty(formTemplate, this, "fileName", locale, args, this.getDefaultValue().getFileName()));
        field.setContentType(FormUtils.getFieldStringProperty(formTemplate, this, "contentType", locale, args, this.getDefaultValue().getContentType()));
        field.setSize(FormUtils.getFieldIntegerProperty(formTemplate, this, "size", locale, args, this.getDefaultValue().getSize()));
        field.setVisibleSeconds(FormUtils.getFieldIntegerProperty(formTemplate, this, "visibleSeconds", locale, args, this.getDefaultValue().getVisibleSeconds()));
        field.setSpoilSeconds(FormUtils.getFieldIntegerProperty(formTemplate, this, "spoilSeconds", locale, args, this.getDefaultValue().getSpoilSeconds()));
        if (null != dataProvider) {
            dataProvider.fillData(field, args);
        }
    }
}
