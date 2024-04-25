package org.bardframework.form.field.view;

import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;
import org.bardframework.form.field.FieldTemplate;

import java.util.Locale;
import java.util.Map;

public class GravatarFieldTemplate extends FieldTemplate<GravatarField> {

    protected GravatarFieldTemplate(String name) {
        super(name);
    }

    @Override
    public void fill(FormTemplate formTemplate, GravatarField field, Map<String, String> args, Locale locale) throws Exception {
        super.fill(formTemplate, field, args, locale);
        field.setIdentifier(FormUtils.getFieldStringProperty(formTemplate, this, "identifier", locale, args, this.getDefaultValues().getIdentifier()));
        field.setSize(FormUtils.getFieldLongProperty(formTemplate, this, "size", locale, args, this.getDefaultValues().getSize()));
        field.setCircle(FormUtils.getFieldBooleanProperty(formTemplate, this, "circle", locale, args, this.getDefaultValues().getCircle()));
    }
}
