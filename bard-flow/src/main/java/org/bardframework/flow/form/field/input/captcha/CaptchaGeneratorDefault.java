package org.bardframework.flow.form.field.input.captcha;

import lombok.Getter;
import org.bardframework.commons.captcha.CaptchaService;
import org.bardframework.commons.captcha.CaptchaType;
import org.bardframework.commons.captcha.GeneratedCaptcha;
import org.bardframework.flow.form.field.input.otp.OtpGenerator;

@Getter
public class CaptchaGeneratorDefault implements OtpGenerator<GeneratedCaptcha> {

    private final CaptchaService captchaService;
    private final CaptchaType type;
    private final int length;

    public CaptchaGeneratorDefault(CaptchaService captchaService, CaptchaType type, int length) {
        this.captchaService = captchaService;
        this.type = type;
        this.length = length;
    }

    @Override
    public GeneratedCaptcha generate() {
        return this.getCaptchaService().generateCaptcha(this.getType(), this.getLength());
    }

    @Override
    public int getLength() {
        return length;
    }

    @Override
    public Boolean isNumber() {
        return type == CaptchaType.ENGLISH_NUMBER;
    }
}
