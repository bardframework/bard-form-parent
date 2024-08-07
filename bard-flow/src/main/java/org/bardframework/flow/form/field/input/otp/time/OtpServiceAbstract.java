package org.bardframework.flow.form.field.input.otp.time;

import lombok.Getter;
import lombok.Setter;
import org.bardframework.flow.form.field.input.otp.OtpGenerator;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;

/*
    https://github.com/google/google-authenticator/wiki/Key-Uri-Format
 */
@Getter
@Setter
public abstract class OtpServiceAbstract implements OtpGenerator<Void> {
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

    @Override
    public Void generate() {
        return null;
    }

    @Override
    public Boolean isNumber() {
        return true;
    }

    @Override
    public int getLength() {
        return length;
    }

}
