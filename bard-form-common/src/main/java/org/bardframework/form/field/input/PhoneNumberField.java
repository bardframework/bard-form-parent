package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;
import org.bardframework.form.field.FieldType;

@Getter
@Setter
public class PhoneNumberField extends InputField<String> {
    private CountrySelectField countrySelectField;
    private Integer maxLength;

    @Override
    public FieldType getType() {
        return InputFieldType.PHONE_NUMBER;
    }
}