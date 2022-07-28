package org.bardframework.form.field.input;

import org.bardframework.form.field.FieldType;

public class OtpField extends InputField<String> {
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

    public String getResendAction() {
        return resendAction;
    }

    public void setResendAction(String resendAction) {
        this.resendAction = resendAction;
    }

    public Boolean getNumber() {
        return number;
    }

    public void setNumber(Boolean number) {
        this.number = number;
    }

    public Boolean getCanEditIdentifier() {
        return canEditIdentifier;
    }

    public void setCanEditIdentifier(Boolean canEditIdentifier) {
        this.canEditIdentifier = canEditIdentifier;
    }
}
