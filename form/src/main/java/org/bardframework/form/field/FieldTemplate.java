package org.bardframework.form.field;

import org.bardframework.commons.utils.ReflectionUtils;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;
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
        F field = this.getEmptyField();
        this.fill(formTemplate, field, args, locale);
        return field;
    }

    protected void fill(FormTemplate formTemplate, F field, Map<String, String> args, Locale locale) throws Exception {
        field.setTitle(FormUtils.getFieldStringProperty(formTemplate, this.getName(), "title", locale, args, null));
        field.setDescription(FormUtils.getFieldStringProperty(formTemplate, this.getName(), "description", locale, args, null));
    }

    protected F getEmptyField() {
        return ReflectionUtils.newInstance(this.fieldClazz);
    }

    public String getName() {
        return name;
    }
}
