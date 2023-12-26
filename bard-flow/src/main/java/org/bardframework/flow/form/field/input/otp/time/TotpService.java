package org.bardframework.flow.form.field.input.otp.time;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.bardframework.commons.utils.StringTemplateUtils;

import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

public class TotpService extends OtpServiceAbstract {
    private static final String KEY_URI_TEMPLATE = "otpauth://totp/::label::?secret=::secret::&issuer=::issuer::&algorithm=::algorithm::&digits=::length::&period=::periodSeconds::";
    private int periodSeconds = 30;
    private int window = 5;

    public String getUri(String issuer, String label, String secretBase32) {
        Map<String, String> args = new HashMap<>();
        args.put("algorithm", this.getAlgorithm());
        args.put("length", String.valueOf(this.getLength()));
        args.put("periodSeconds", String.valueOf(this.getPeriodSeconds()));
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

    private long generateTOTP(long time, byte[] secret) throws GeneralSecurityException {
        final byte[] data = new byte[8];
        for (int i = 8; i-- > 0; time >>>= 8) {
            data[i] = (byte) time;
        }

        final byte[] result = this.hmac(data, secret);
        final int offset = result[result.length - 1] & 0xF;
        long truncatedResult = 0L;
        for (int i = 0; i < 4; ++i) {
            truncatedResult = truncatedResult << 8 | result[offset + i] & 0xFF;
        }

        return (truncatedResult & 0x7FFFFFFF) % (long) Math.pow(10, this.getLength());
    }

    /**
     * This method generates a TOTP1 value for the given set of parameters.
     *
     * @param key  the shared secret, HEX encoded
     * @param time a value that reflects a time
     * @return a numeric String in base 10 that includes
     */
    private long generateTOTP2(long time, byte[] key) throws GeneralSecurityException {
        // Using the counter
        // First 8 bytes are for the movingFactor Compliant with base RFC 4226 (HOTP)
        StringBuilder timeBuilder = new StringBuilder(String.valueOf(time));
        while (timeBuilder.length() < 16) {
            timeBuilder.insert(0, "0");
        }
        // Get the HEX in a Byte[]
        byte[] msg = ArrayUtils.remove(new BigInteger("10" + timeBuilder, 16).toByteArray(), 0);

        byte[] hash = this.hmac(msg, key);

        // put selected bytes into result int
        int offset = hash[hash.length - 1] & 0xf;

        int binary = ((hash[offset] & 0x7f) << 24) | ((hash[offset + 1] & 0xff) << 16) | ((hash[offset + 2] & 0xff) << 8) | (hash[offset + 3] & 0xff);
        return binary % (long) Math.pow(10, this.getLength());
    }

    @Override
    public boolean verify(String otp, byte[] secret) throws GeneralSecurityException {
        if (!NumberUtils.isDigits(otp)) {
            return false;
        }
        int code = Integer.parseInt(otp);
        final long timeSeconds = System.currentTimeMillis() / 30_000;
        for (int i = -this.getWindow(); i <= this.getWindow(); ++i) {
            long generatedOtp = this.generateTOTP(timeSeconds + i, secret);
            if (generatedOtp == code) {
                return true;
            }
        }
        return false;
    }

    public int getWindow() {
        return window;
    }

    public void setWindow(int window) {
        if (window < 1 || window > 10) {
            throw new IllegalStateException("Window size provided not allowed: " + window);
        }
        this.window = window;
    }

    public int getPeriodSeconds() {
        return periodSeconds;
    }

    public void setPeriodSeconds(int periodSeconds) {
        this.periodSeconds = periodSeconds;
    }
}
