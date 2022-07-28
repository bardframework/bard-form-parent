package org.bardframework.flow.form.field.input.otp.time;

import org.bardframework.flow.form.field.input.otp.OtpFieldTemplate;

import java.util.Base64;
import java.util.Map;

public class TotpFieldTemplate extends OtpFieldTemplate {
    public static final String TOTP_SECRET_KEY = "totp_secret";

    private final OtpServiceAbstract otpService;

    protected TotpFieldTemplate(String name, OtpServiceAbstract otpService) {
        super(name, otpService.getLength());
        this.otpService = otpService;
        this.isNumber = true;
    }

    @Override
    protected boolean isValidOtp(String flowToken, String otp, Map<String, String> flowData) throws Exception {
        String totpSecretBase64 = flowData.get(TOTP_SECRET_KEY);
        byte[] totpSecret = Base64.getDecoder().decode(totpSecretBase64);
        return otpService.verify(otp, totpSecret);
    }
}
