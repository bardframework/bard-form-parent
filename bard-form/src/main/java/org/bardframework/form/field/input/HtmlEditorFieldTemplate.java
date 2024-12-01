package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;

@Getter
@Setter
public class HtmlEditorFieldTemplate extends InputFieldTemplateAbstract<HtmlEditorField, String> {

    public HtmlEditorFieldTemplate(String name) {
        super(name);
    }

    @Override
    public boolean isValid(String flowToken, HtmlEditorField field, String value, Map<String, Object> flowData) {
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
    public void fill(FormTemplate formTemplate, HtmlEditorField field, Map<String, Object> values, Map<String, Object> args, Locale locale) throws Exception {
        super.fill(formTemplate, field, values, args, locale);
        field.setMaxSize(FormUtils.getFieldIntegerProperty(formTemplate, this, "maxSize", locale, args, this.getDefaultValue().getMaxSize()));
    }
}
