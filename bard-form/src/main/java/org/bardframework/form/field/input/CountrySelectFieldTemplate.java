package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;

import java.util.Locale;
import java.util.Map;

@Getter
@Setter
public class CountrySelectFieldTemplate extends InputFieldTemplateAbstract<CountrySelectField, String> {

    public CountrySelectFieldTemplate(String name) {
        super(name);
    }

    @Override
    public boolean isValid(String flowToken, CountrySelectField field, String value, Map<String, Object> flowData) {
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
    public void fill(FormTemplate formTemplate, CountrySelectField field, Map<String, Object> values, Map<String, Object> args, Locale locale) throws Exception {
        super.fill(formTemplate, field, values, args, locale);
        field.setAvailableCountries(FormUtils.getFieldListProperty(formTemplate, this.getName(), "availableCountries", locale, args, this.getDefaultValue().getAvailableCountries()));
        field.setExcludeCountries(FormUtils.getFieldListProperty(formTemplate, this.getName(), "excludeCountries", locale, args, this.defaultValue.getExcludeCountries()));
    }
}
