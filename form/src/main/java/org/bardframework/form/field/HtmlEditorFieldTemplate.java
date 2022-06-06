package org.bardframework.form.field;

import org.apache.commons.lang3.StringUtils;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;
import org.bardframework.form.common.field.HtmlEditorField;
import org.bardframework.form.field.base.FormFieldTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;

public class HtmlEditorFieldTemplate extends FormFieldTemplate<HtmlEditorField, String> {

    protected HtmlEditorFieldTemplate(String name) {
        super(name);
    }

    @Override
    public boolean isValid(HtmlEditorField field, String value) {
        if (StringUtils.isBlank(value) && Boolean.TRUE.equals(field.getDisable())) {
            LOGGER.debug("field [{}] is required, but it's value is empty", field.getName());
            return false;
        }
        if (null != field.getMaxSize() && null != value && value.getBytes(StandardCharsets.UTF_8).length > field.getMaxSize()) {
            LOGGER.debug("field [{}] max size is [{}] bytes, but it's value length is [{}] bytes.", field.getName(), field.getMaxSize(), value.getBytes(StandardCharsets.UTF_8).length);
            return false;
        }
        return true;
    }

    @Override
    public void fill(FormTemplate formTemplate, HtmlEditorField field, Map<String, String> args, Locale locale) throws Exception {
        super.fill(formTemplate, field, args, locale);
        field.setMaxSize(FormUtils.getFieldIntegerProperty(formTemplate, this.getName(), "maxSize", locale, args, null));
    }

    @Override
    public String toValue(String value) {
        return value;
    }
}