package org.bardframework.form.field.view;

import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;
import org.bardframework.form.field.FieldTemplate;

import java.util.Locale;
import java.util.Map;

public class ReadOnlyFieldTemplate extends FieldTemplate<ReadonlyField> {

    public ReadOnlyFieldTemplate(String name) {
        super(name);
    }

    @Override
    public void fill(FormTemplate formTemplate, ReadonlyField field, Map<String, String> args, Locale locale) throws Exception {
        super.fill(formTemplate, field, args, locale);
        field.setMask(FormUtils.getFieldStringProperty(formTemplate, this.getName(), "mask", locale, args, null));
    }
}
