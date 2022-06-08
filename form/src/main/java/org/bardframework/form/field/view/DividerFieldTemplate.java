package org.bardframework.form.field.view;

import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;
import org.bardframework.form.field.FieldTemplate;

import java.util.Locale;
import java.util.Map;

public class DividerFieldTemplate extends FieldTemplate<DividerField> {

    protected DividerFieldTemplate(String name) {
        super(name);
    }

    @Override
    public void fill(FormTemplate formTemplate, DividerField field, Map<String, String> args, Locale locale) throws Exception {
        super.fill(formTemplate, field, args, locale);
        field.setLabel(FormUtils.getFieldStringProperty(formTemplate, this.getName(), "label", locale, args, null));
    }
}
