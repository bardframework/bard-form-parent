package org.bardframework.form.field.input.captcha;

import org.bardframework.commons.captcha.CaptchaService;
import org.bardframework.commons.captcha.CaptchaType;
import org.bardframework.commons.captcha.GeneratedCaptcha;

public class CaptchaGeneratorDefault implements CaptchaGenerator {

    private final CaptchaService captchaService;
    private final CaptchaType type;
    private final int length;

    public CaptchaGeneratorDefault(CaptchaService captchaService, CaptchaType type, int length) {
        this.captchaService = captchaService;
        this.type = type;
        this.length = length;
    }

    @Override
    public GeneratedCaptcha generate() throws Exception {
        return this.getCaptchaService().generateCaptcha(this.getType(), this.getLength());
    }

    public CaptchaService getCaptchaService() {
        return captchaService;
    }

    public CaptchaType getType() {
        return type;
    }

    @Override
    public int getLength() {
        return length;
    }
}
