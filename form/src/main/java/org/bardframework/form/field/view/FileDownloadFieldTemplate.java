package org.bardframework.form.field.view;

import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;
import org.bardframework.form.field.FieldTemplate;

import java.util.Locale;
import java.util.Map;

public class FileDownloadFieldTemplate extends FieldTemplate<FileDownloadField> {

    public FileDownloadFieldTemplate(String name) {
        super(name);
    }

    @Override
    public void fill(FormTemplate formTemplate, FileDownloadField field, Map<String, String> args, Locale locale) throws Exception {
        super.fill(formTemplate, field, args, locale);
        field.setContentType(FormUtils.getFieldStringProperty(formTemplate, this.getName(), "contentType", locale, args, null));
        field.setSize(FormUtils.getFieldIntegerProperty(formTemplate, this.getName(), "size", locale, args, null));
        field.setUrl(FormUtils.getFieldStringProperty(formTemplate, this.getName(), "url", locale, args, null));
    }

}
