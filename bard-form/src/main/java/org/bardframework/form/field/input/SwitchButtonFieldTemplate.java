package org.bardframework.form.field.input;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class SwitchButtonFieldTemplate extends InputFieldTemplate<SwitchButtonField, Boolean> {

    protected SwitchButtonFieldTemplate(String name) {
        super(name);
    }

    @Override
    public boolean isValid(SwitchButtonField field, Boolean value, Map<String, String> flowData) {
        return true;
    }

    @Override
    public Boolean toValue(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return Boolean.parseBoolean(value);
    }

}