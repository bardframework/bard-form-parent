package org.bardframework.flow.form.field.input.otp.time;

import org.bardframework.commons.utils.StringTemplateUtils;

import java.util.HashMap;
import java.util.Map;

public class HotpService extends OtpServiceAbstract {
    private static final String KEY_URI_TEMPLATE = "otpauth://hotp/::label::?secret=::secret::&issuer=::issuer::&algorithm=::algorithm::&digits=::length::&counter=::counter::";

    public String getUri(String issuer, String label, String secretBase32, int counter) {
        Map<String, String> args = new HashMap<>();
        args.put("algorithm", this.getAlgorithm());
        args.put("length", String.valueOf(this.getLength()));
        args.put("counter", String.valueOf(counter));
        //
        args.put("label", label);
        args.put("issuer", issuer);
        args.put("secret", secretBase32);
        return StringTemplateUtils.fillTemplate(KEY_URI_TEMPLATE, args);
    }

    @Override
    public String generate(byte[] secret) {
        return null;
    }

    @Override
    public boolean verify(String otp, byte[] secret) {
        return false;
    }

}
