package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;
import org.bardframework.form.field.FieldType;

@Getter
@Setter
public class CaptchaField extends OtpField {

    private String audioAction;

    @Override
    public FieldType getType() {
        return InputFieldType.CAPTCHA;
    }
}