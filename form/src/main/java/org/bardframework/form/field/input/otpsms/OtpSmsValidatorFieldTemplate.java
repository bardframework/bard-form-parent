package org.bardframework.form.field.input.otpsms;

import org.bardframework.form.field.input.TextFieldTemplate;
import org.bardframework.form.processor.messagesender.creator.MessageCreator;
import org.bardframework.form.processor.messagesender.otp.OtpGenerator;
import org.bardframework.form.processor.messagesender.sender.MessageSenderSms;
import org.springframework.context.MessageSource;

import java.util.List;
import java.util.Map;

public class OtpSmsValidatorFieldTemplate extends TextFieldTemplate {

    static final String RESEND_COUNT_KEY = "S_otp_resend_count";
    static final String SEND_TIME_KEY = "S_otp_sent_time";
    static final String SMS_RESOLVE_COUNT_KEY = "S_otp_resolve_count";
    static final String GENERATED_OTP_KEY = "X_G_OTP";

    protected MessageSource messageSource;
    private String resendAction;

    public OtpSmsValidatorFieldTemplate(String name, MessageCreator messageCreator, MessageSenderSms messageSender, OtpGenerator otpGenerator, int maxTryToResolveCount, int maxResendOtpCount, int resendIntervalSeconds, String errorMessageCode) {
        super(name);
        OtpSmsSenderProcessor otpSmsSenderProcessor = new OtpSmsSenderProcessor(otpGenerator, messageCreator, messageSender, errorMessageCode);
        this.setPreProcessors(List.of(otpSmsSenderProcessor));
        this.setPostProcessors(List.of(new OtpValidatorProcessor(this, maxTryToResolveCount)));
        this.setResendAction("otp-resend");
        this.setActionProcessors(Map.of(this.getResendAction(), List.of(new ResendOtpProcessor(otpSmsSenderProcessor, maxResendOtpCount, resendIntervalSeconds))));
        this.setPersistentValue(false);
    }

    public String getResendAction() {
        return resendAction;
    }

    public void setResendAction(String resendAction) {
        this.resendAction = resendAction;
    }

    public MessageSource getMessageSource() {
        return messageSource;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }


}
