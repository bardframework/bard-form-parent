package org.bardframework.flow.form.field.input.otp;

import org.bardframework.flow.processor.FormProcessorAbstract;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.Map;

public class OtpResendProcessor extends FormProcessorAbstract {
    public static final String RESEND_COUNT_KEY = "S_otp_resend_count";

    private final OtpSenderProcessor otpSenderProcessor;
    private final int maxResendOtpCount;
    private final int resendIntervalSeconds;

    public OtpResendProcessor(OtpSenderProcessor otpSenderProcessor, int maxResendOtpCount, int resendIntervalSeconds) {
        this.otpSenderProcessor = otpSenderProcessor;
        this.maxResendOtpCount = maxResendOtpCount;
        this.resendIntervalSeconds = resendIntervalSeconds;
    }

    @Override
    public void process(String flowToken, Map<String, String> flowData, Map<String, String> formData, Locale locale, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
        int resendCount = flowData.containsKey(RESEND_COUNT_KEY) ? Integer.parseInt(flowData.get(RESEND_COUNT_KEY)) : 0;
        if (resendCount > maxResendOtpCount) {
            httpResponse.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            LOGGER.error("flowToken[{}], max otp resend exceed", flowToken);
            return;
        }
        long sentTime = flowData.containsKey(OtpSenderProcessor.SEND_TIME_KEY) ? Long.parseLong(flowData.get(OtpSenderProcessor.SEND_TIME_KEY)) : 0;
        long remainSeconds = resendIntervalSeconds - ((System.currentTimeMillis() - sentTime) / 1000);
        if (remainSeconds > 0) {
            LOGGER.error("flowToken[{}], try to resend otp, before resend interval!", flowToken);
            httpResponse.getWriter().println(remainSeconds);
            httpResponse.setStatus(HttpStatus.TOO_EARLY.value());
            return;
        }
        otpSenderProcessor.process(flowToken, flowData, formData, locale, httpRequest, httpResponse);
    }
}