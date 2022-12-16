package org.bardframework.flow.form.field.input.otp;

public class OtpGeneratorFixed implements OtpGenerator<String> {
    private final String fixedOtp;

    public OtpGeneratorFixed(String fixedOtp) {
        this.fixedOtp = fixedOtp;
    }

    @Override
    public String generate() {
        return fixedOtp;
    }

    @Override
    public int getLength() {
        return fixedOtp.length();
    }

    @Override
    public Boolean isNumber() {
        return true;
    }
}