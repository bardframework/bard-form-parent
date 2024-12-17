package org.bardframework.form.field.view;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;
import org.bardframework.form.field.FieldTemplate;
import org.bardframework.form.field.value.FileFieldDataProvider;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

@Getter
@Setter
public class FileDownloadFieldTemplate extends FieldTemplate<FileDownloadField> {

    protected FileFieldDataProvider dataProvider;

    public FileDownloadFieldTemplate(String name) {
        super(name);
    }

    @Override
    public void fill(FormTemplate formTemplate, FileDownloadField field, Map<String, Object> values, Map<String, Object> args, Locale locale) throws Exception {
        super.fill(formTemplate, field, values, args, locale);

        FileDownloadField.FileModel defaultValue = CollectionUtils.isEmpty(this.getDefaultValue().getFiles()) ? new FileDownloadField.FileModel() : this.getDefaultValue().getFiles().get(0);
        FileDownloadField.FileModel fileModel = null;
        String data = FormUtils.getFieldStringProperty(formTemplate, this, "data", locale, args, defaultValue.getData());
        if (data != null) {
            fileModel = new FileDownloadField.FileModel();
            fileModel.setData(data);
            fileModel.setFileName(FormUtils.getFieldStringProperty(formTemplate, this, "fileName", locale, args, defaultValue.getFileName()));
            fileModel.setContentType(FormUtils.getFieldStringProperty(formTemplate, this, "contentType", locale, args, defaultValue.getContentType()));
            fileModel.setSize(FormUtils.getFieldIntegerProperty(formTemplate, this, "size", locale, args, defaultValue.getSize()));
            fileModel.setDescription(FormUtils.getFieldStringProperty(formTemplate, this, "description", locale, args, defaultValue.getDescription()));
        }
        if (null != dataProvider) {
            dataProvider.fillData(field, args);
        }
        if (null == field.getFiles()) {
            field.setFiles(new ArrayList<>());
        }
        if (null != fileModel) {
            field.getFiles().add(fileModel);
        }
    }

}
