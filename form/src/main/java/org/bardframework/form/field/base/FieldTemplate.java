package org.bardframework.form.field.base;

import org.bardframework.commons.utils.ReflectionUtils;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;
import org.bardframework.form.common.field.base.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.Map;

public abstract class FieldTemplate<F extends Field> {
    protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    protected final Class<F> fieldClazz;

    protected final String name;

    protected FieldTemplate(String name) {
        this.name = name;
        this.fieldClazz = ReflectionUtils.getGenericArgType(this.getClass(), 0);
    }

    public F toField(FormTemplate formTemplate, Map<String, String> args, Locale locale) throws Exception {
        F field = ReflectionUtils.newInstance(this.fieldClazz);
        this.fill(formTemplate, field, args, locale);
        return field;
    }

    protected void fill(FormTemplate formTemplate, F field, Map<String, String> args, Locale locale) throws Exception {
        field.setLabel(FormUtils.getFieldStringProperty(formTemplate, this.getName(), "label", locale, args, null));
    }

    public String getName() {
        return name;
    }
}
