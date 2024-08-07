package org.bardframework.flow.form.field.input.otp;

import com.mifmif.common.regex.Generex;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OtpGeneratorRegex implements OtpGenerator<String> {
    private final Generex generex;
    private final int length;
    private Boolean number;

    public OtpGeneratorRegex(String otpRegex, int length) {
        this.generex = new Generex(otpRegex);
        this.length = length;
    }

    @Override
    public String generate() {
        return generex.random();
    }

    @Override
    public int getLength() {
        return length;
    }

    @Override
    public Boolean isNumber() {
        return number;
    }
}