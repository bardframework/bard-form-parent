package org.bardframework.flow.form.field.input.otpsms;

import org.apache.commons.lang3.StringUtils;
import org.bardframework.flow.exception.FlowDataValidationException;
import org.bardframework.flow.exception.InvalidateFlowException;
import org.bardframework.flow.processor.FormProcessorAbstract;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.Map;

public class OtpValidatorProcessor extends FormProcessorAbstract {
    private final OtpSmsValidatorFieldTemplate fieldTemplate;
    private final int maxTryToResolveCount;

    public OtpValidatorProcessor(OtpSmsValidatorFieldTemplate fieldTemplate, int maxTryToResolveCount) {
        this.fieldTemplate = fieldTemplate;
        this.maxTryToResolveCount = maxTryToResolveCount;
    }

    @Override
    public void process(String flowToken, Map<String, String> flowData, Map<String, String> formData, Locale locale, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
        String enteredOtp = formData.get(fieldTemplate.getName());
        String generatedOtp = flowData.get(OtpSmsValidatorFieldTemplate.GENERATED_OTP_KEY);
        if (StringUtils.isBlank(generatedOtp)) {
            throw new InvalidateFlowException(flowToken, "generated otp is empty, can't validate otp.");
        }
        if (generatedOtp.equalsIgnoreCase(enteredOtp.trim())) {
            flowData.remove(OtpSmsValidatorFieldTemplate.GENERATED_OTP_KEY);
            flowData.remove(OtpSmsValidatorFieldTemplate.SMS_RESOLVE_COUNT_KEY);
            return;
        }
        int smsResolveTryCount = flowData.containsKey(OtpSmsValidatorFieldTemplate.SMS_RESOLVE_COUNT_KEY) ? Integer.parseInt(flowData.get(OtpSmsValidatorFieldTemplate.SMS_RESOLVE_COUNT_KEY)) : 0;
        if (smsResolveTryCount >= maxTryToResolveCount) {
            throw new InvalidateFlowException(flowToken, "too many try to resolve sms, terminating flow...");
        }
        flowData.put(OtpSmsValidatorFieldTemplate.SMS_RESOLVE_COUNT_KEY, String.valueOf(smsResolveTryCount + 1));
        throw new FlowDataValidationException().addFiledError(fieldTemplate.getName());
    }
}