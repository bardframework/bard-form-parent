package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@Getter
@Setter
abstract class FileUploadFieldTemplateAbstract<F extends FileUploadField> extends InputFieldTemplateAbstract<F, List<String>> {

    public FileUploadFieldTemplateAbstract(String name) {
        super(name, true);
    }

    public FileUploadFieldTemplateAbstract(String name, boolean persistentValue) {
        super(name, persistentValue);
    }

    @Override
    public boolean isValid(String flowToken, F field, List<String> values, Map<String, Object> flowData) throws Exception {
        if (CollectionUtils.isEmpty(values)) {
            if (Boolean.TRUE.equals(field.getRequired())) {
                log.debug("field [{}] is required, but it's value is empty", field.getName());
                return false;
            }
            return true;
        }
        if (null != field.getMinCount() && values.size() < field.getMinCount()) {
            log.debug("data count[{}] of field[{}] is less than minimum[{}]", values.size(), field.getName(), field.getMinCount());
            return false;
        }
        if (null != field.getMaxCount() && values.size() > field.getMaxCount()) {
            log.debug("data count[{}] of field[{}] is greater than maximum[{}]", values.size(), field.getName(), field.getMaxCount());
            return false;
        }
        //TODO implement other validators
        return true;
    }

    @Override
    public void fill(FormTemplate formTemplate, F field, Map<String, Object> values, Map<String, Object> args, Locale locale) throws Exception {
        super.fill(formTemplate, field, values, args, locale);
        field.setMinSize(FormUtils.getFieldIntegerProperty(formTemplate, this, "minSize", locale, args, this.getDefaultValue().getMinSize()));
        field.setMaxSize(FormUtils.getFieldIntegerProperty(formTemplate, this, "maxSize", locale, args, this.getDefaultValue().getMaxSize()));
        field.setMinTotalSize(FormUtils.getFieldIntegerProperty(formTemplate, this, "minTotalSize", locale, args, this.getDefaultValue().getMinTotalSize()));
        field.setMaxTotalSize(FormUtils.getFieldIntegerProperty(formTemplate, this, "maxTotalSize", locale, args, this.getDefaultValue().getMaxTotalSize()));
        field.setMinCount(FormUtils.getFieldByteProperty(formTemplate, this, "minCount", locale, args, this.getDefaultValue().getMinCount()));
        field.setMaxCount(FormUtils.getFieldByteProperty(formTemplate, this, "maxCount", locale, args, this.getDefaultValue().getMaxCount()));
        field.setValidContentTypes(FormUtils.getFieldListProperty(formTemplate, this, "validContentTypes", locale, args, this.getDefaultValue().getValidContentTypes()));
        field.setUploadAction(this.getDefaultValue().getUploadAction());
        field.setDownloadAction(this.getDefaultValue().getDownloadAction());
    }
}
