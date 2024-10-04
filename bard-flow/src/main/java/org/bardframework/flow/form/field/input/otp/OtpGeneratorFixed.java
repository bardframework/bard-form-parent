package org.bardframework.flow.form.field.input.otp;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.math.NumberUtils;

@Getter
@Setter
public class OtpGeneratorFixed implements OtpGenerator<String> {
    private String value;
    private Boolean number;

    public OtpGeneratorFixed() {
    }

    public OtpGeneratorFixed(String value) {
        this.value = value;
        this.number = NumberUtils.isDigits(this.value);
    }

    @Override
    public String generate() {
        return value;
    }

    @Override
    public int getLength() {
        return value.length();
    }

    @Override
    public Boolean isNumber() {
        return number;
    }
}