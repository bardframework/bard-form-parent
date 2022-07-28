package org.bardframework.form.field.input;

import org.bardframework.form.field.FieldType;

public class OtpField extends InputField<String> {
    private int length;
    private Integer resendIntervalSeconds;
    private Boolean canEditIdentifier;
    private Boolean isNumber;

    @Override
    public String toString(String value) {
        return value;
    }

    @Override
    public FieldType getType() {
        return InputFieldType.OTP;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public Integer getResendIntervalSeconds() {
        return resendIntervalSeconds;
    }

    public void setResendIntervalSeconds(Integer resendIntervalSeconds) {
        this.resendIntervalSeconds = resendIntervalSeconds;
    }

    public Boolean getCanEditIdentifier() {
        return canEditIdentifier;
    }

    public void setCanEditIdentifier(Boolean canEditIdentifier) {
        this.canEditIdentifier = canEditIdentifier;
    }

    public Boolean getIsNumber() {
        return isNumber;
    }

    public void setIsNumber(Boolean isNumber) {
        this.isNumber = isNumber;
    }
}
