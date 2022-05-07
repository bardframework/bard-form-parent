package org.bardframework.form.field.base;

import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;
import org.bardframework.form.common.field.base.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.Map;

public abstract class FieldTemplate<F extends Field> {
    protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    protected final String name;

    protected FieldTemplate(String name) {
        this.name = name;
    }

    public abstract F getEmptyField();

    public F toField(FormTemplate formTemplate, Map<String, String> args, Locale locale) throws Exception {
        F field = this.getEmptyField();
        this.fill(formTemplate, field, args, locale);
        return field;
    }

    protected void fill(FormTemplate formTemplate, F field, Map<String, String> args, Locale locale) throws Exception {
        field.setLabel(FormUtils.getFieldValue(formTemplate, this.getName(), "label", locale, args));
    }

    public String getName() {
        return name;
    }
}
