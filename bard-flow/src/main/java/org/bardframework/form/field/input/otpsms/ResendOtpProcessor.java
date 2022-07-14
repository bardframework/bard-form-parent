package org.bardframework.form.field.input.otpsms;

import org.bardframework.flow.processor.FormProcessorAbstract;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.Map;

public class ResendOtpProcessor extends FormProcessorAbstract {

    private final OtpSmsSenderProcessor otpSmsSenderProcessor;
    private final int maxResendOtpCount;
    private final int resendIntervalSeconds;

    public ResendOtpProcessor(OtpSmsSenderProcessor otpSmsSenderProcessor, int maxResendOtpCount, int resendIntervalSeconds) {
        this.otpSmsSenderProcessor = otpSmsSenderProcessor;
        this.maxResendOtpCount = maxResendOtpCount;
        this.resendIntervalSeconds = resendIntervalSeconds;
    }

    @Override
    public void process(String flowToken, Map<String, String> flowData, Map<String, String> formData, Locale locale, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
        int resendCount = flowData.containsKey(OtpSmsValidatorFieldTemplate.RESEND_COUNT_KEY) ? Integer.parseInt(flowData.get(OtpSmsValidatorFieldTemplate.RESEND_COUNT_KEY)) : 0;
        if (resendCount > maxResendOtpCount) {
            httpResponse.setStatus(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS.value());
            LOGGER.error("flowToken[{}], max otp resend exceed", flowToken);
            return;
        }
        long sentTime = flowData.containsKey(OtpSmsValidatorFieldTemplate.SEND_TIME_KEY) ? Long.parseLong(flowData.get(OtpSmsValidatorFieldTemplate.SEND_TIME_KEY)) : 0;
        long remainSeconds = resendIntervalSeconds - ((System.currentTimeMillis() - sentTime) / 1000);
        if (remainSeconds > 0) {
            LOGGER.error("flowToken[{}], try to resend otp, before resend interval!", flowToken);
            httpResponse.getWriter().println(remainSeconds);
            httpResponse.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            return;
        }
        otpSmsSenderProcessor.process(flowToken, flowData, formData, locale, httpRequest, httpResponse);
    }
}