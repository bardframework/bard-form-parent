package org.bardframework.flow.form.field.input.otp;

import com.mifmif.common.regex.Generex;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.math.NumberUtils;

@Getter
@Setter
public class OtpGeneratorRegex implements OtpGenerator<String> {
    private Generex generex;
    private int length;
    private Boolean number;

    public OtpGeneratorRegex() {
    }

    public OtpGeneratorRegex(String pattern) {
        this.setPattern(pattern);
        this.init();
    }

    private void init() {
        String sampleOtp = this.generate();
        this.length = sampleOtp.length();
        this.number = NumberUtils.isCreatable(sampleOtp);
    }

    @Override
    public String generate() {
        return generex.random();
    }

    @Override
    public int getLength() {
        return length;
    }

    @Override
    public Boolean isNumber() {
        return number;
    }

    public void setPattern(String pattern) {
        this.generex = new Generex(pattern);
        this.init();
    }
}