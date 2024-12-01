package org.bardframework.form.field.view;

import lombok.Getter;
import lombok.Setter;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.field.FieldTemplate;

import java.util.Locale;
import java.util.Map;

@Getter
@Setter
public class DividerFieldTemplate extends FieldTemplate<DividerField> {

    public DividerFieldTemplate(String name) {
        super(name);
    }

    @Override
    public void fill(FormTemplate formTemplate, DividerField field, Map<String, Object> values, Map<String, Object> args, Locale locale) throws Exception {
        super.fill(formTemplate, field, values, args, locale);
    }
}
