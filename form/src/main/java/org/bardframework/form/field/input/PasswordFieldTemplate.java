package org.bardframework.form.field.input;

import org.apache.commons.lang3.StringUtils;
import org.bardframework.form.field.FormFieldTemplate;

public class PasswordFieldTemplate extends FormFieldTemplate<PasswordField, String> {

    public PasswordFieldTemplate(String name) {
        super(name);
    }

    @Override
    public boolean isValid(PasswordField field, String value) {
        if (StringUtils.isBlank(value)) {
            if (Boolean.TRUE.equals(field.getRequired())) {
                LOGGER.debug("field [{}] is required, but it's value is empty", field.getName());
                return false;
            }
            return true;
        }
        return true;
    }

    @Override
    public String toValue(String value) {
        return null;
    }

    @Override
    protected PasswordField getEmptyField() {
        return new PasswordField();
    }
}
