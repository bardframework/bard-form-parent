package org.bardframework.form.field.view;

import org.bardframework.form.FormTemplate;
import org.bardframework.form.field.FieldTemplate;

import javax.servlet.http.HttpServletRequest;
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
