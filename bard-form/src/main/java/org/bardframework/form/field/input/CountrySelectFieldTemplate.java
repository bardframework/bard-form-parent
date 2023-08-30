package org.bardframework.form.field.input;

import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;

import java.util.Locale;
import java.util.Map;

@Getter
public class CountrySelectFieldTemplate extends InputFieldTemplate<CountrySelectField, String> {

    protected CountrySelectFieldTemplate(String name) {
        super(name);
    }

    @Override
    public boolean isValid(String flowToken, CountrySelectField field, String value, Map<String, String> flowData) {
        if (null == value) {
            if (Boolean.TRUE.equals(field.getRequired())) {
                log.debug("field [{}] is required, but it's value is empty", field.getName());
                return false;
            }
            return true;
        }
        String countryCode = this.getCountryCode(value);
        if (CollectionUtils.isNotEmpty(field.getAvailableCountries()) && !field.getAvailableCountries().contains(countryCode)) {
            log.debug("country code[{}] is not match with specified available countries[{}]", countryCode, field.getAvailableCountries());
            return false;
        }
        if (CollectionUtils.isNotEmpty(field.getExcludeCountries()) && field.getExcludeCountries().contains(countryCode)) {
            log.debug("country code[{}] is in exclude countries[{}]", countryCode, field.getExcludeCountries());
            return false;
        }
        return true;
    }

    protected String getCountryCode(String value) {
        return value;
    }

    @Override
    public void fill(FormTemplate formTemplate, CountrySelectField field, Map<String, String> args, Locale locale) throws Exception {
        field.setAvailableCountries(FormUtils.getFieldListProperty(formTemplate, this.getName(), "availableCountries", locale, args, this.getDefaultValues().getAvailableCountries()));
        field.setExcludeCountries(FormUtils.getFieldListProperty(formTemplate, this.getName(), "excludeCountries", locale, args, this.defaultValues.getExcludeCountries()));
        super.fill(formTemplate, field, args, locale);
    }

    @Override
    public String toValue(String value) {
        return value;
    }
}
