package org.bardframework.flow.form.field.input.otp.time;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;

/*
    https://github.com/google/google-authenticator/wiki/Key-Uri-Format
 */
public abstract class OtpServiceAbstract {
    /*
        SHA1 (Default)
        SHA256
        SHA512
     */
    private String algorithm = "SHA1";
    private String hmacAlgorithm = "HmacSHA1";
    private String hmacProvider = "SunJCE";
    private String rngAlgorithm = "SHA1PRNG";
    private String rngProvider = "SUN";
    /*
        6 or 8
     */
    private int length = 6;
    private int secretSize = 30;

    public abstract String generate(byte[] secret);

    public abstract boolean verify(String otp, byte[] secret) throws GeneralSecurityException;

    protected byte[] hmac(final byte[] input, byte[] secret) throws GeneralSecurityException {
        final Mac mac = Mac.getInstance(this.getHmacAlgorithm(), this.getHmacProvider());
        mac.init(new SecretKeySpec(secret, this.getHmacAlgorithm()));
        return mac.doFinal(input);
    }

    public byte[] generateSecret() throws GeneralSecurityException {
        final byte[] secret = new byte[this.getSecretSize()];
        SecureRandom random = SecureRandom.getInstance(this.getRngAlgorithm(), this.getRngProvider());
        random.nextBytes(secret);
        return secret;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getHmacAlgorithm() {
        return hmacAlgorithm;
    }

    public void setHmacAlgorithm(String hmacAlgorithm) {
        this.hmacAlgorithm = hmacAlgorithm;
    }

    public String getHmacProvider() {
        return hmacProvider;
    }

    public void setHmacProvider(String hmacProvider) {
        this.hmacProvider = hmacProvider;
    }

    public String getRngAlgorithm() {
        return rngAlgorithm;
    }

    public void setRngAlgorithm(String rngAlgorithm) {
        this.rngAlgorithm = rngAlgorithm;
    }

    public String getRngProvider() {
        return rngProvider;
    }

    public void setRngProvider(String rngProvider) {
        this.rngProvider = rngProvider;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getSecretSize() {
        return secretSize;
    }

    public void setSecretSize(int secretSize) {
        this.secretSize = secretSize;
    }
}
