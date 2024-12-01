package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class SwitchButtonFieldTemplate extends InputFieldTemplateAbstract<SwitchButtonField, Boolean> {

    public SwitchButtonFieldTemplate(String name) {
        super(name);
    }

    @Override
    public boolean isValid(String flowToken, SwitchButtonField field, Boolean value, Map<String, Object> flowData) {
        return true;
    }
}