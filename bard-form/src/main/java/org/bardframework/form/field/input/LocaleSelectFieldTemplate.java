package org.bardframework.form.field.input;

import org.apache.commons.collections4.CollectionUtils;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;

import java.util.Locale;
import java.util.Map;

public class LocaleSelectFieldTemplate extends InputFieldTemplate<LocaleSelectField, String> {
    protected LocaleSelectFieldTemplate(String name) {
        super(name);
    }

    public boolean isValid(String flowToken, LocaleSelectField field, String value, Map<String, String> flowData) {
        if (null == value) {
            if (Boolean.TRUE.equals(field.getRequired())) {
                this.log.debug("field [{}] is required, but it's value is empty", field.getName());
                return false;
            } else {
                return true;
            }
        } else {
            String locale = this.getLocale(value);
            if (CollectionUtils.isNotEmpty(field.getAvailableLocales()) && !field.getAvailableLocales().contains(locale)) {
                this.log.debug("locale code[{}] is not match with specified available locales[{}]", locale, field.getAvailableLocales());
                return false;
            } else if (CollectionUtils.isNotEmpty(field.getExcludeLocales()) && field.getExcludeLocales().contains(locale)) {
                this.log.debug("locale code[{}] is in exclude locales[{}]", locale, field.getExcludeLocales());
                return false;
            } else {
                return true;
            }
        }
    }

    public void fill(FormTemplate formTemplate, LocaleSelectField field, Map<String, String> args, Locale locale) throws Exception {
        super.fill(formTemplate, field, args, locale);
        field.setAvailableLocales(FormUtils.getFieldListProperty(formTemplate, this.getName(), "availableLocale", locale, args, this.getDefaultValues().getAvailableLocales()));
        field.setExcludeLocales(FormUtils.getFieldListProperty(formTemplate, this.getName(), "excludeLocale", locale, args, this.defaultValues.getExcludeLocales()));
    }

    public String toValue(String value) {
        return value;
    }

    protected String getLocale(String value) {
        return value;
    }

}
