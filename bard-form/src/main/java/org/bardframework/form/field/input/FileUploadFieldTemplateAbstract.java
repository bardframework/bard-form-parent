package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
abstract class FileUploadFieldTemplateAbstract<F extends FileUploadField> extends InputFieldTemplateAbstract<F, List<String>> {

    protected FileUploadFieldTemplateAbstract(String name) {
        super(name, true);
    }

    public FileUploadFieldTemplateAbstract(String name, boolean persistentValue) {
        super(name, persistentValue);
    }

    @Override
    public void fill(FormTemplate formTemplate, F field, Map<String, String> values, Locale locale) throws Exception {
        super.fill(formTemplate, field, values, locale);
        field.setMinSize(FormUtils.getFieldIntegerProperty(formTemplate, this, "minSize", locale, values, this.getDefaultValue().getMinSize()));
        field.setMaxSize(FormUtils.getFieldIntegerProperty(formTemplate, this, "maxSize", locale, values, this.getDefaultValue().getMaxSize()));
        field.setTotalSize(FormUtils.getFieldIntegerProperty(formTemplate, this, "totalSize", locale, values, this.getDefaultValue().getTotalSize()));
        field.setCount(FormUtils.getFieldByteProperty(formTemplate, this, "count", locale, values, this.getDefaultValue().getCount()));
        field.setContentTypes(FormUtils.getFieldListProperty(formTemplate, this, "contentTypes", locale, values, this.getDefaultValue().getContentTypes()));
        field.setUploadAction(this.getDefaultValue().getUploadAction());
        field.setDownloadAction(this.getDefaultValue().getDownloadAction());
        field.setSubmitType(this.getDefaultValue().getSubmitType());
    }

    @Override
    public List<String> toValue(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return Arrays.stream(value.split(InputField.SEPARATOR)).map(String::trim).collect(Collectors.toList());
    }
}
