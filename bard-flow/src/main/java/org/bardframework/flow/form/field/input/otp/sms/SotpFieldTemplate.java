package org.bardframework.flow.form.field.input.otp.sms;

import org.bardframework.flow.form.field.input.otp.GeneratedOtpFieldTemplate;
import org.bardframework.flow.processor.messagesender.creator.MessageCreator;
import org.bardframework.flow.processor.messagesender.otp.OtpGenerator;
import org.bardframework.flow.processor.messagesender.sender.MessageSenderSms;

public class SotpFieldTemplate extends GeneratedOtpFieldTemplate {
    public SotpFieldTemplate(String name, MessageCreator messageCreator, MessageSenderSms messageSender, OtpGenerator otpGenerator, int maxTryToResolveCount, int maxResendOtpCount, int resendIntervalSeconds, String errorMessageCode) {
        super(name, messageCreator, messageSender, otpGenerator, maxTryToResolveCount, maxResendOtpCount, resendIntervalSeconds, errorMessageCode);
    }
}
