package org.bardframework.form.field.view;

import jakarta.servlet.http.HttpServletRequest;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.field.FieldTemplate;

import java.util.Locale;
import java.util.Map;

public class DividerFieldTemplate extends FieldTemplate<DividerField> {

    protected DividerFieldTemplate(String name) {
        super(name);
    }

    @Override
    public void fill(FormTemplate formTemplate, DividerField field, Map<String, String> args, Locale locale, HttpServletRequest httpRequest) throws Exception {
        super.fill(formTemplate, field, args, locale, httpRequest);
    }
}
