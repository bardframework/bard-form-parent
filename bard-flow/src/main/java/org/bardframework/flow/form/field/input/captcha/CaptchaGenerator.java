package org.bardframework.flow.form.field.input.captcha;

import org.bardframework.commons.captcha.GeneratedCaptcha;

public interface CaptchaGenerator {

    GeneratedCaptcha generate() throws Exception;

    int getLength();
}
