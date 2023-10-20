package org.bardframework.flow.form.field.input.phonenumber;

import lombok.Getter;
import lombok.Setter;
import org.bardframework.form.field.FieldType;
import org.bardframework.form.field.input.CountrySelectField;
import org.bardframework.form.field.input.InputField;
import org.bardframework.form.field.input.InputFieldType;

@Getter
@Setter
public class PhoneNumberField extends InputField<String> {
    private CountrySelectField countrySelectField;
    private Integer maxLength;

    @Override
    public String toString(String value) {
        return value;
    }

    @Override
    public FieldType getType() {
        return InputFieldType.PHONE_NUMBER;
    }
}