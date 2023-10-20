package org.bardframework.form.field.input;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.bardframework.commons.utils.data.LocaleCode;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class LocaleSelectFieldTemplate extends InputFieldTemplate<LocaleSelectField, List<String>> {

    protected LocaleSelectFieldTemplate(String name) {
        super(name);
    }

    @Override
    public boolean isValid(String flowToken, LocaleSelectField field, List<String> values, Map<String, String> flowData) {
        if (CollectionUtils.isEmpty(values)) {
            if (Boolean.TRUE.equals(field.getRequired())) {
                log.debug("field [{}] is required, but it's value is empty", field.getName());
                return false;
            }
            return true;
        }
        if (values.size() > field.getMaxCount()) {
            log.debug("selected option count[{}] of field[{}] is greater than maximum[{}]", values.size(), field.getName(), field.getMaxCount());
            return false;
        }
        if (values.stream().anyMatch(value -> EnumUtils.getEnum(LocaleCode.class, value, null) == null)) {
            log.debug("entry locale {} is not exists in locale code", values);
            return false;
        }
        if (CollectionUtils.isNotEmpty(field.getAvailableLocales()) && field.getAvailableLocales().stream().noneMatch(values::contains)) {
            log.debug("entry locale [{}] is not match with specified available locales[{}]", values, field.getAvailableLocales());
            return false;
        }
        if (CollectionUtils.isNotEmpty(field.getExcludeLocales()) && field.getExcludeLocales().stream().anyMatch(values::contains)) {
            log.debug("entry locale [{}] is in exclude locales[{}]", values, field.getExcludeLocales());
            return false;
        }
        return true;
    }

    @Override
    public void fill(FormTemplate formTemplate, LocaleSelectField field, Map<String, String> args, Locale locale) throws Exception {
        super.fill(formTemplate, field, args, locale);
        field.setAvailableLocales(FormUtils.getFieldListProperty(formTemplate, this.getName(), "availableLocale", locale, args, this.getDefaultValues().getAvailableLocales()));
        field.setExcludeLocales(FormUtils.getFieldListProperty(formTemplate, this.getName(), "excludeLocale", locale, args, this.defaultValues.getExcludeLocales()));
        field.setMaxCount(FormUtils.getFieldIntegerProperty(formTemplate, this.getName(), "maxCount", locale, args, this.getDefaultValues().getMaxCount()));
    }

    @Override
    public List<String> toValue(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return Arrays.stream(value.split(InputField.SEPARATOR)).map(String::trim).collect(Collectors.toList());
    }
}
