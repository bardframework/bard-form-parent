package org.bardframework.form.field.view;

import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;
import org.bardframework.form.field.FieldTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Map;

public class ReadOnlyFieldTemplate extends FieldTemplate<ReadonlyField> {

    public ReadOnlyFieldTemplate(String name) {
        super(name);
    }

    @Override
    public void fill(FormTemplate formTemplate, ReadonlyField field, Map<String, String> args, Locale locale, HttpServletRequest httpRequest) throws Exception {
        super.fill(formTemplate, field, args, locale, httpRequest);
        field.setMask(FormUtils.getFieldStringProperty(formTemplate, this, "mask", locale, args, null));
    }
}
