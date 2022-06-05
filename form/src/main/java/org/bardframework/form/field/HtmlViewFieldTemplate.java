package org.bardframework.form.field;

import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;
import org.bardframework.form.common.field.HtmlViewField;
import org.bardframework.form.field.base.FieldTemplate;

import java.util.Locale;
import java.util.Map;

public class HtmlViewFieldTemplate extends FieldTemplate<HtmlViewField> {

    public HtmlViewFieldTemplate(String name) {
        super(name);
    }

    @Override
    public void fill(FormTemplate formTemplate, HtmlViewField field, Map<String, String> args, Locale locale) throws Exception {
        super.fill(formTemplate, field, args, locale);
        field.setHtml(FormUtils.getFieldStringProperty(formTemplate, this.getName(), "html", locale, args, null));
    }

}
