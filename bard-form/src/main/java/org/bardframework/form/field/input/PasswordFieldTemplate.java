package org.bardframework.form.field.input;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

@Slf4j
public class PasswordFieldTemplate extends InputFieldTemplate<PasswordField, String> {

    public PasswordFieldTemplate(String name) {
        super(name);
    }

    @Override
    public boolean isValid(String flowToken, PasswordField field, String value, Map<String, String> flowData) {
        if (StringUtils.isBlank(value)) {
            if (Boolean.TRUE.equals(field.getRequired())) {
                log.debug("field [{}] is required, but it's value is empty", field.getName());
                return false;
            }
            return true;
        }
        return true;
    }

    @Override
    public String toValue(String value) {
        return value;
    }

    @Override
    protected PasswordField getEmptyField() {
        return new PasswordField();
    }
}
