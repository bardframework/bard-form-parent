package org.bardframework.form.field;

import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;
import org.bardframework.form.common.field.DividerField;
import org.bardframework.form.field.base.FieldTemplate;

import java.util.Locale;
import java.util.Map;

public class DividerFieldTemplate extends FieldTemplate<DividerField> {

    protected DividerFieldTemplate(String name) {
        super(name);
    }

    @Override
    public DividerField getEmptyField() {
        return new DividerField();
    }

    @Override
    public void fill(FormTemplate formTemplate, DividerField field, Map<String, String> args, Locale locale) throws Exception {
        super.fill(formTemplate, field, args, locale);
        field.setLabel(FormUtils.getFieldValue(formTemplate, this.getName(), "label", locale, args));
    }
}
