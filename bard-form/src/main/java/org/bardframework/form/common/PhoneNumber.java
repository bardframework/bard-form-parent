package org.bardframework.form.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhoneNumber {
    private String rawValue;
    private String countryAlphaCode;
    private int countryCode;
    private long number;

    public String getFullNumber() {
        return "%d%d".formatted(this.countryCode, this.number);
    }
}
