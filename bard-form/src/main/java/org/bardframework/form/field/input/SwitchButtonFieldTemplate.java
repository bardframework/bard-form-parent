package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

@Getter
@Setter
public class SwitchButtonFieldTemplate extends InputFieldTemplateAbstract<SwitchButtonField, Boolean> {

    public SwitchButtonFieldTemplate(String name) {
        super(name);
    }

    @Override
    public boolean isValid(String flowToken, SwitchButtonField field, Boolean value, Map<String, String> flowData) {
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