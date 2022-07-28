package org.bardframework.flow.form.field.input.otp;

import org.bardframework.flow.processor.messagesender.MessageSenderProcessor;
import org.bardframework.flow.processor.messagesender.creator.MessageCreator;
import org.bardframework.flow.processor.messagesender.otp.OtpGenerator;
import org.bardframework.flow.processor.messagesender.sender.MessageSender;

import java.util.Map;

public class OtpSenderProcessor extends MessageSenderProcessor {

    public static final String GENERATED_OTP_KEY = "X_G_OTP";
    public static final String SEND_TIME_KEY = "S_otp_sent_time";

    private final OtpGenerator otpGenerator;

    public OtpSenderProcessor(OtpGenerator otpGenerator, MessageCreator messageCreator, MessageSender messageSender, String errorMessageCode) {
        super(messageCreator, messageSender, errorMessageCode);
        this.otpGenerator = otpGenerator;
    }

    @Override
    protected void beforeSend(Map<String, String> flowData) {
        flowData.put(GENERATED_OTP_KEY, otpGenerator.generateOtp());
    }

    @Override
    protected void afterSend(Map<String, String> flowData) {
        flowData.put(SEND_TIME_KEY, String.valueOf(System.currentTimeMillis()));
    }
}