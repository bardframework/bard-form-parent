package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.bardframework.form.field.FieldType;

@Slf4j
@Getter
@Setter
@ToString
public abstract class OtpField extends InputField<String> {
    private int length;
    private String resendAction;
    private Integer resendIntervalSeconds;
    private Boolean canEditIdentifier;
    private Boolean number;

    @Override
    public String toString(String value) {
        return value;
    }

    @Override
    public FieldType getType() {
        return InputFieldType.OTP;
    }
}
