package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.bardframework.form.field.FieldType;

import java.util.List;

@Slf4j
@Getter
@Setter
@ToString
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
}