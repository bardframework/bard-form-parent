package org.bardframework.form.field;

import org.apache.commons.lang3.StringUtils;
import org.bardframework.form.common.field.SingleCheckBoxField;
import org.bardframework.form.field.base.FormFieldTemplate;

public class SingleCheckboxFieldTemplate extends FormFieldTemplate<SingleCheckBoxField, Boolean> {

    protected SingleCheckboxFieldTemplate(String name) {
        super(name);
    }

    @Override
    public boolean isValid(SingleCheckBoxField field, Boolean value) {
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