package org.bardframework.form.field.view;

import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;
import org.bardframework.form.field.FieldTemplate;

import java.util.Locale;
import java.util.Map;

public class GravatarFieldTemplate extends FieldTemplate<GravatarField> {

    public GravatarFieldTemplate(String name) {
        super(name);
    }

    @Override
    public void fill(FormTemplate formTemplate, GravatarField field, Map<String, Object> values, Map<String, Object> args, Locale locale) throws Exception {
        super.fill(formTemplate, field, values, args, locale);
        field.setIdentifier(FormUtils.getFieldStringProperty(formTemplate, this, "identifier", locale, args, this.getDefaultValue().getIdentifier()));
        field.setSize(FormUtils.getFieldLongProperty(formTemplate, this, "size", locale, args, this.getDefaultValue().getSize()));
        field.setCircle(FormUtils.getFieldBooleanProperty(formTemplate, this, "circle", locale, args, this.getDefaultValue().getCircle()));
    }
}
