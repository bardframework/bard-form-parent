package org.bardframework.form.processor.messagesender.otp;

public class OtpGeneratorMock implements OtpGenerator {
    private final String fixedOtp;

    public OtpGeneratorMock(String fixedOtp) {
        this.fixedOtp = fixedOtp;
    }

    @Override
    public String generateOtp() {
        return fixedOtp;
    }
}