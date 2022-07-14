package org.bardframework.flow.processor.messagesender.otp;

import com.mifmif.common.regex.Generex;

public class OtpGeneratorRegex implements OtpGenerator {
    private final Generex generex;

    public OtpGeneratorRegex(String otpRegex) {
        this.generex = new Generex(otpRegex);
    }

    @Override
    public String generateOtp() {
        return generex.random();
    }
}