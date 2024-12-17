package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bardframework.form.field.FieldType;

@Getter
@Setter
@ToString
public class OtpField extends InputField<String> {
    private int length;
    private String resendAction;
    private Integer resendIntervalSeconds;
    private Boolean number;

    @Override
    public FieldType getType() {
        return InputFieldType.OTP;
    }
}
