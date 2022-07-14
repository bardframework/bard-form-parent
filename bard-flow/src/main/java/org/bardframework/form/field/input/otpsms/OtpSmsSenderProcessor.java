package org.bardframework.form.field.input.otpsms;

import org.bardframework.flow.processor.messagesender.MessageSenderProcessor;
import org.bardframework.flow.processor.messagesender.creator.MessageCreator;
import org.bardframework.flow.processor.messagesender.otp.OtpGenerator;
import org.bardframework.flow.processor.messagesender.sender.MessageSenderSms;

import java.util.Map;

public class OtpSmsSenderProcessor extends MessageSenderProcessor {

    private final OtpGenerator otpGenerator;

    public OtpSmsSenderProcessor(OtpGenerator otpGenerator, MessageCreator messageCreator, MessageSenderSms messageSender, String errorMessageCode) {
        super(messageCreator, messageSender, errorMessageCode);
        this.otpGenerator = otpGenerator;
    }

    @Override
    protected void beforeSend(Map<String, String> flowData) {
        flowData.put(OtpSmsValidatorFieldTemplate.GENERATED_OTP_KEY, otpGenerator.generateOtp());
    }

    @Override
    protected void afterSend(Map<String, String> flowData) {
        flowData.put(OtpSmsValidatorFieldTemplate.SEND_TIME_KEY, String.valueOf(System.currentTimeMillis()));
    }
}