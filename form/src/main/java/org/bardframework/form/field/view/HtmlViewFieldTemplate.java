package org.bardframework.form.field.view;

import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;
import org.bardframework.form.field.FieldTemplate;

import java.util.Locale;
import java.util.Map;

public class HtmlViewFieldTemplate extends FieldTemplate<HtmlViewField> {

    public HtmlViewFieldTemplate(String name) {
        super(name);
    }

    @Override
    public void fill(FormTemplate formTemplate, HtmlViewField field, Map<String, String> args, Locale locale) throws Exception {
        super.fill(formTemplate, field, args, locale);
        field.setHtml(FormUtils.getFieldStringProperty(formTemplate, this, "html", locale, args, null));
    }

}
