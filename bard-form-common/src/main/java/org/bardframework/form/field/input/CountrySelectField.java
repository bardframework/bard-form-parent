package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bardframework.form.field.FieldType;

import java.util.List;

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