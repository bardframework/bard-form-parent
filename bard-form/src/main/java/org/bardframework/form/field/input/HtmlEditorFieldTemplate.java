package org.bardframework.form.field.input;

import org.apache.commons.lang3.StringUtils;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;

public class HtmlEditorFieldTemplate extends InputFieldTemplate<HtmlEditorField, String> {

    protected HtmlEditorFieldTemplate(String name) {
        super(name);
    }

    @Override
    public boolean isValid(HtmlEditorField field, String value, Map<String, String> args) {
        if (StringUtils.isBlank(value)) {
            if (Boolean.TRUE.equals(field.getRequired())) {
                LOGGER.debug("field [{}] is required, but it's value is empty", field.getName());
                return false;
            }
            return true;
        }
        if (null != field.getMaxSize() && value.getBytes(StandardCharsets.UTF_8).length > field.getMaxSize()) {
            LOGGER.debug("field [{}] max size is [{}] bytes, but it's value length is [{}] bytes.", field.getName(), field.getMaxSize(), value.getBytes(StandardCharsets.UTF_8).length);
            return false;
        }
        return true;
    }

    @Override
    public void fill(FormTemplate formTemplate, HtmlEditorField field, Map<String, String> args, Locale locale, HttpServletRequest httpRequest) throws Exception {
        super.fill(formTemplate, field, args, locale, httpRequest);
        field.setMaxSize(FormUtils.getFieldIntegerProperty(formTemplate, this, "maxSize", locale, args, this.getDefaultValues().getMaxSize()));
    }

    @Override
    public String toValue(String value) {
        return value;
    }
}
