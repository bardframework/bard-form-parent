package org.bardframework.form.field.input.phonenumber;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PhoneNumber {
    private String rawValue;
    private String countryAlphaCode;
    private int countryCode;
    private long number;

    public String getFullNumber() {
        return "%d%d".formatted(this.countryCode, this.number);
    }
}
