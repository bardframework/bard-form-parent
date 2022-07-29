package org.bardframework.form.field.input;

import org.apache.commons.collections4.CollectionUtils;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Map;

public class CountrySelectFieldTemplate extends InputFieldTemplate<CountrySelectField, String> {

    protected CountrySelectFieldTemplate(String name) {
        super(name);
    }

    @Override
    public boolean isValid(String flowToken, CountrySelectField field, String value, Map<String, String> flowData) {
        if (null == value) {
            if (Boolean.TRUE.equals(field.getRequired())) {
                LOGGER.debug("field [{}] is required, but it's value is empty", field.getName());
                return false;
            }
            return true;
        }
        if (CollectionUtils.isNotEmpty(field.getAvailableCountries()) && !field.getAvailableCountries().contains(value)) {
            LOGGER.debug("country code[{}] is not match with specified available countries[{}]", value, field.getAvailableCountries());
            return false;
        }
        if (CollectionUtils.isNotEmpty(field.getExcludeCountries()) && field.getExcludeCountries().contains(value)) {
            LOGGER.debug("country code[{}] is in exclude countries[{}]", value, field.getExcludeCountries());
            return false;
        }
        return true;
    }

    @Override
    public void fill(FormTemplate formTemplate, CountrySelectField field, Map<String, String> args, Locale locale, HttpServletRequest httpRequest) throws Exception {
        field.setAvailableCountries(FormUtils.getFieldListProperty(formTemplate, this.getName(), "availableCountries", locale, args, this.getDefaultValues().getAvailableCountries()));
        field.setExcludeCountries(FormUtils.getFieldListProperty(formTemplate, this.getName(), "excludeCountries", locale, args, this.defaultValues.getExcludeCountries()));
        super.fill(formTemplate, field, args, locale, httpRequest);
    }

    @Override
    public String toValue(String value) {
        return value;
    }
}