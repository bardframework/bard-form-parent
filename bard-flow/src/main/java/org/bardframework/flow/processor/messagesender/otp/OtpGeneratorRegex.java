package org.bardframework.flow.processor.messagesender.otp;

import com.mifmif.common.regex.Generex;

public class OtpGeneratorRegex implements OtpGenerator {
    private final Generex generex;
    private final int length;

    public OtpGeneratorRegex(String otpRegex, int length) {
        this.generex = new Generex(otpRegex);
        this.length = length;
    }

    @Override
    public String generateOtp() {
        return generex.random();
    }

    @Override
    public int length() {
        return length;
    }
}