package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bardframework.form.field.FieldType;

import java.util.List;

@Getter
@Setter
@ToString
public class CountryMultiSelectField extends InputField<List<String>> {
    private List<String> availableCountries;
    private List<String> excludeCountries;

    @Override
    public FieldType getType() {
        return InputFieldType.COUNTRY_MULTI_SELECT;
    }
}