package org.bardframework.flow.form.field.input.otp.time;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import org.bardframework.flow.form.field.input.otp.OtpFieldTemplate;
import org.bardframework.form.field.input.OtpField;

import java.util.Base64;
import java.util.Locale;
import java.util.Map;

@Getter
@Setter
public class TotpFieldTemplate extends OtpFieldTemplate<OtpField, Void> {
    public static final String TOTP_SECRET_KEY = "X_TOTP_SECRET";
    protected final OtpServiceAbstract otpService;
    protected String secretAttributeName = TOTP_SECRET_KEY;

    public TotpFieldTemplate(String name, int maxTryToResolveCount, OtpServiceAbstract otpService) {
        super(name, otpService, maxTryToResolveCount);
        this.otpService = otpService;
    }

    @Override
    protected String getResendAction() {
        return null;
    }

    @Override
    protected void send(String flowToken, Map<String, Object> flowData, Void otp, Locale locale, HttpServletResponse httpResponse) throws Exception {
        /*
            do nothing
         */

    }

    @Override
    protected boolean isValidOtp(String flowToken, String otp, Map<String, Object> flowData) throws Exception {
        String totpSecretBase64 = flowData.get(this.getSecretAttributeName()).toString();
        byte[] totpSecret = Base64.getDecoder().decode(totpSecretBase64);
        return otpService.verify(otp, totpSecret);
    }

    @Override
    protected String getOtpMaxTryToResolveCountErrorMessage() {
        return "totp.error.max.resolve.exceeded";
    }

    @Override
    protected String getMaxSendCountErrorMessage() {
        return "totp.error.max.send.exceeded";
    }
}
