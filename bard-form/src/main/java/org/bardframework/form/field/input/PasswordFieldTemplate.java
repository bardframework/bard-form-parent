package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

@Getter
@Setter
public class PasswordFieldTemplate extends InputFieldTemplateAbstract<PasswordField, String> {

    public PasswordFieldTemplate(String name) {
        super(name);
    }

    @Override
    public boolean isValid(String flowToken, PasswordField field, String value, Map<String, Object> flowData) {
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
    protected PasswordField getEmptyField() {
        return new PasswordField();
    }
}
