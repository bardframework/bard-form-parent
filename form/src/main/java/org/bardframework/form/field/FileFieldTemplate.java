package org.bardframework.form.field;

import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;
import org.bardframework.form.common.field.FileField;
import org.bardframework.form.field.base.FieldTemplate;

import java.util.Locale;
import java.util.Map;

public class FileFieldTemplate extends FieldTemplate<FileField> {

    public FileFieldTemplate(String name) {
        super(name);
    }

    @Override
    public void fill(FormTemplate formTemplate, FileField field, Map<String, String> args, Locale locale) throws Exception {
        super.fill(formTemplate, field, args, locale);
        field.setContentType(FormUtils.getFieldStringProperty(formTemplate, this.getName(), "contentType", locale, args, null));
        field.setSize(FormUtils.getFieldIntegerProperty(formTemplate, this.getName(), "size", locale, args, null));
        field.setUrl(FormUtils.getFieldStringProperty(formTemplate, this.getName(), "url", locale, args, null));
    }

}
