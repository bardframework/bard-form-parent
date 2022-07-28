package org.bardframework.flow.form.field.input.otp;

import org.bardframework.flow.form.field.input.FlowInputFieldTemplate;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.field.input.OtpField;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Map;

public abstract class OtpFieldTemplate extends FlowInputFieldTemplate<OtpField, String> {

    protected final int length;
    protected Integer resendIntervalSeconds;
    protected Boolean canEditIdentifier;
    protected Boolean isNumber;

    protected OtpFieldTemplate(String name, int length) {
        super(name, false);
        this.length = length;
    }

    @Override
    public final boolean isValid(String flowToken, OtpField field, String value, Map<String, String> flowData) throws Exception {
        if (null == value) {
            if (Boolean.TRUE.equals(field.getRequired())) {
                LOGGER.debug("field [{}] is required, but it's value is empty", field.getName());
                return false;
            }
            return true;
        }
        value = value.trim();
        if (value.length() != this.getLength()) {
            LOGGER.debug("field [{}] length is [{}], than not equal to it's value[{}] length.", field.getName(), field.getLength(), value.length());
            return false;
        }
        return this.isValidOtp(flowToken, value, flowData);
    }

    protected abstract boolean isValidOtp(String flowToken, String otp, Map<String, String> flowData) throws Exception;

    @Override
    public void fill(FormTemplate formTemplate, OtpField field, Map<String, String> values, Locale locale, HttpServletRequest httpRequest) throws Exception {
        super.fill(formTemplate, field, values, locale, httpRequest);
        field.setLength(this.getLength());
        field.setResendIntervalSeconds(this.getResendIntervalSeconds());
        field.setCanEditIdentifier(this.getCanEditIdentifier());
        field.setIsNumber(this.getIsNumber());
    }

    @Override
    public String toValue(String value) {
        return value;
    }

    public int getLength() {
        return length;
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
