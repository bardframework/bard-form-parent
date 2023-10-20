package org.bardframework.flow.form.field.input.otp.sendsms;

import lombok.Getter;
import lombok.Setter;
import org.bardframework.form.field.FieldType;
import org.bardframework.form.field.input.InputField;
import org.bardframework.form.field.input.InputFieldType;

@Getter
@Setter
public class SsotpField extends InputField<String> {
    private String otp;
    private Boolean number;

    @Override
    public FieldType getType() {
        return InputFieldType.SSOTP;
    }

    @Override
    public String toString(String value) {
        return value;
    }
}
