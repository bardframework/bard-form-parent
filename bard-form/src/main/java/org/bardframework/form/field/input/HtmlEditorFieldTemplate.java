package org.bardframework.form.field.input;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;

@Slf4j
@Getter
public class HtmlEditorFieldTemplate extends InputFieldTemplate<HtmlEditorField, String> {

    protected HtmlEditorFieldTemplate(String name) {
        super(name);
    }

    @Override
    public boolean isValid(String flowToken, HtmlEditorField field, String value, Map<String, String> flowData) {
        if (StringUtils.isBlank(value)) {
            if (Boolean.TRUE.equals(field.getRequired())) {
                log.debug("field [{}] is required, but it's value is empty", field.getName());
                return false;
            }
            return true;
        }
        if (null != field.getMaxSize() && value.getBytes(StandardCharsets.UTF_8).length > field.getMaxSize()) {
            log.debug("field [{}] max size is [{}] bytes, but it's value length is [{}] bytes.", field.getName(), field.getMaxSize(), value.getBytes(StandardCharsets.UTF_8).length);
            return false;
        }
        return true;
    }

    @Override
    public void fill(FormTemplate formTemplate, HtmlEditorField field, Map<String, String> values, Locale locale, HttpServletRequest httpRequest) throws Exception {
        super.fill(formTemplate, field, values, locale, httpRequest);
        field.setMaxSize(FormUtils.getFieldIntegerProperty(formTemplate, this, "maxSize", locale, values, this.getDefaultValues().getMaxSize()));
    }

    @Override
    public String toValue(String value) {
        return value;
    }
}
