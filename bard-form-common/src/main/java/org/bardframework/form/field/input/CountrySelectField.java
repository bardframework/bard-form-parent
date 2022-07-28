package org.bardframework.form.field.input;

import org.bardframework.form.field.FieldType;

import java.util.List;

public class CountrySelectField extends InputField<String> {
    private List<String> availableCountries;
    private List<String> excludeCountries;

    @Override
    public String toString(String value) {
        return value;
    }

    @Override
    public FieldType getType() {
        return InputFieldType.COUNTRY_SELECT;
    }

    public List<String> getAvailableCountries() {
        return availableCountries;
    }

    public void setAvailableCountries(List<String> availableCountries) {
        this.availableCountries = availableCountries;
    }

    public List<String> getExcludeCountries() {
        return excludeCountries;
    }

    public void setExcludeCountries(List<String> excludeCountries) {
        this.excludeCountries = excludeCountries;
    }
}