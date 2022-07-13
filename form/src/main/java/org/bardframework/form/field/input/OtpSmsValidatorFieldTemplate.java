package org.bardframework.form.field.input;

import org.apache.commons.lang3.StringUtils;
import org.bardframework.form.exception.FlowDataValidationException;
import org.bardframework.form.exception.InvalidateFlowException;
import org.bardframework.form.processor.FormProcessor;
import org.bardframework.form.processor.messagesender.MessageSenderProcessor;
import org.bardframework.form.processor.messagesender.creator.MessageCreator;
import org.bardframework.form.processor.messagesender.otp.OtpGenerator;
import org.bardframework.form.processor.messagesender.sender.MessageSenderSms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class OtpSmsValidatorFieldTemplate extends TextFieldTemplate {
    private static final Logger LOGGER = LoggerFactory.getLogger(OtpSmsValidatorFieldTemplate.class);
    private static final String RESEND_COUNT_KEY = "S_otp_resend_count";
    private static final String SEND_TIME_KEY = "S_otp_sent_time";
    private final static String SMS_RESOLVE_COUNT_KEY = "S_otp_resolve_count";

    private final int maxTryToResolveCount;
    private final int maxResendOtpCount;
    private final int resendIntervalSeconds;
    private final OtpGenerator otpGenerator;
    private final OtpSmsSenderProcessor otpSmsSenderProcessor;
    protected MessageSource messageSource;
    private String generatedOtpKey;
    private String resendAction;

    public OtpSmsValidatorFieldTemplate(String name, MessageCreator messageCreator, MessageSenderSms messageSender, OtpGenerator otpGenerator, int maxTryToResolveCount, int maxResendOtpCount, int resendIntervalSeconds, String errorMessageCode) {
        super(name);
        this.maxTryToResolveCount = maxTryToResolveCount;
        this.maxResendOtpCount = maxResendOtpCount;
        this.resendIntervalSeconds = resendIntervalSeconds;
        this.otpGenerator = otpGenerator;
        this.otpSmsSenderProcessor = new OtpSmsSenderProcessor(messageCreator, messageSender, errorMessageCode);
        this.setResendAction("otp-resend");
        this.setGeneratedOtpKey("X_G_OTP");
        this.setPreProcessors(List.of(otpSmsSenderProcessor));
        this.setPostProcessors(List.of(new OtpValidatorProcessor()));
        this.setActionProcessors(Map.of(this.getResendAction(), List.of(new ResendOtpProcessor())));
        this.setPersistentValue(false);
    }

    public String getResendAction() {
        return resendAction;
    }

    public void setResendAction(String resendAction) {
        this.resendAction = resendAction;
    }

    public String getGeneratedOtpKey() {
        return generatedOtpKey;
    }

    public void setGeneratedOtpKey(String generatedOtpKey) {
        this.generatedOtpKey = generatedOtpKey;
    }

    public int getMaxTryToResolveCount() {
        return maxTryToResolveCount;
    }

    public int getMaxResendOtpCount() {
        return maxResendOtpCount;
    }

    public int getResendIntervalSeconds() {
        return resendIntervalSeconds;
    }

    public MessageSource getMessageSource() {
        return messageSource;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    private class OtpSmsSenderProcessor extends MessageSenderProcessor {
        public OtpSmsSenderProcessor(MessageCreator messageCreator, MessageSenderSms messageSender, String errorMessageCode) {
            super(messageCreator, messageSender, errorMessageCode);
        }

        @Override
        protected void beforeSend(Map<String, String> flowData) {
            flowData.put(generatedOtpKey, otpGenerator.generateOtp());
        }

        @Override
        protected void afterSend(Map<String, String> flowData) {
            flowData.put(SEND_TIME_KEY, String.valueOf(System.currentTimeMillis()));
        }
    }

    private class ResendOtpProcessor implements FormProcessor {

        @Override
        public void process(String flowToken, Map<String, String> flowData, Map<String, String> formData, Locale locale, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
            int resendCount = flowData.containsKey(RESEND_COUNT_KEY) ? Integer.parseInt(flowData.get(RESEND_COUNT_KEY)) : 0;
            if (resendCount > OtpSmsValidatorFieldTemplate.this.getMaxResendOtpCount()) {
                httpResponse.setStatus(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS.value());
                LOGGER.error("flowToken[{}], max otp resend exceed", flowToken);
                return;
            }
            long sentTime = flowData.containsKey(SEND_TIME_KEY) ? Long.parseLong(flowData.get(SEND_TIME_KEY)) : 0;
            long remainSeconds = OtpSmsValidatorFieldTemplate.this.getResendIntervalSeconds() - ((System.currentTimeMillis() - sentTime) / 1000);
            if (remainSeconds > 0) {
                LOGGER.error("flowToken[{}], try to resend otp, before resend interval!", flowToken);
                httpResponse.getWriter().println(remainSeconds);
                httpResponse.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                return;
            }
            otpSmsSenderProcessor.process(flowToken, flowData, formData, locale, httpRequest, httpResponse);
        }
    }

    private class OtpValidatorProcessor implements FormProcessor {
        @Override
        public void process(String flowToken, Map<String, String> flowData, Map<String, String> formData, Locale locale, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
            String enteredOtp = formData.get(OtpSmsValidatorFieldTemplate.this.getName());
            String generatedOtp = flowData.get(generatedOtpKey);
            if (StringUtils.isBlank(generatedOtp)) {
                throw new InvalidateFlowException(flowToken, "generated otp is empty, can't validate otp.");
            }
            if (generatedOtp.equalsIgnoreCase(enteredOtp.trim())) {
                flowData.remove(generatedOtpKey);
                flowData.remove(SMS_RESOLVE_COUNT_KEY);
                return;
            }
            int smsResolveTryCount = flowData.containsKey(SMS_RESOLVE_COUNT_KEY) ? Integer.parseInt(flowData.get(SMS_RESOLVE_COUNT_KEY)) : 0;
            if (smsResolveTryCount >= OtpSmsValidatorFieldTemplate.this.getMaxTryToResolveCount()) {
                throw new InvalidateFlowException(flowToken, "too many try to resolve sms, terminating flow...");
            }
            flowData.put(SMS_RESOLVE_COUNT_KEY, String.valueOf(smsResolveTryCount + 1));
            throw new FlowDataValidationException().addFiledError(OtpSmsValidatorFieldTemplate.this.getName());
        }
    }
}
