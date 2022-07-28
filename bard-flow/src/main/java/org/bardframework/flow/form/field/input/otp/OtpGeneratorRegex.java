package org.bardframework.flow.form.field.input.otp;

import com.mifmif.common.regex.Generex;

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

    public Boolean getNumber() {
        return number;
    }

    public void setNumber(Boolean number) {
        this.number = number;
    }

    @Override
    public Boolean isNumber() {
        return number;
    }
}