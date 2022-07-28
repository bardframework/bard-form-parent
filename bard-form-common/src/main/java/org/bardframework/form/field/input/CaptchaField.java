package org.bardframework.form.field.input;

import org.bardframework.form.field.FieldType;

public class CaptchaField extends OtpField {

    @Override
    public FieldType getType() {
        return InputFieldType.CAPTCHA;
    }
}