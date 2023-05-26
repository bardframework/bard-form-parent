package org.bardframework.form.field.view;

import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;
import org.bardframework.form.field.FieldTemplate;
import org.bardframework.form.field.value.FileFieldDataProvider;

import java.util.Locale;
import java.util.Map;

public class FileDownloadFieldTemplate extends FieldTemplate<FileDownloadField> {

    protected FileFieldDataProvider dataProvider;

    public FileDownloadFieldTemplate(String name) {
        super(name);
    }

    @Override
    public void fill(FormTemplate formTemplate, FileDownloadField field, Map<String, String> args, Locale locale) throws Exception {
        super.fill(formTemplate, field, args, locale);
        field.setSrc(FormUtils.getFieldStringProperty(formTemplate, this, "src", locale, args, this.getDefaultValues().getSrc()));
        field.setFileName(FormUtils.getFieldStringProperty(formTemplate, this, "fileName", locale, args, this.getDefaultValues().getFileName()));
        field.setContentType(FormUtils.getFieldStringProperty(formTemplate, this, "contentType", locale, args, this.getDefaultValues().getContentType()));
        field.setSize(FormUtils.getFieldIntegerProperty(formTemplate, this, "size", locale, args, this.getDefaultValues().getSize()));
        if (null != dataProvider) {
            dataProvider.fillData(field, args);
        }
    }

    public FileFieldDataProvider getDataProvider() {
        return dataProvider;
    }

    public void setDataProvider(FileFieldDataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }
}
