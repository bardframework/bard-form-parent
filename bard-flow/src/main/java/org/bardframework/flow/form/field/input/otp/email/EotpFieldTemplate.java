package org.bardframework.flow.form.field.input.otp.email;

import org.bardframework.flow.form.field.input.otp.GeneratedOtpFieldTemplate;
import org.bardframework.flow.processor.messagesender.creator.MessageCreator;
import org.bardframework.flow.processor.messagesender.otp.OtpGenerator;
import org.bardframework.flow.processor.messagesender.sender.MessageSenderEmail;

public class EotpFieldTemplate extends GeneratedOtpFieldTemplate {
    public EotpFieldTemplate(String name, MessageCreator messageCreator, MessageSenderEmail messageSender, OtpGenerator otpGenerator, int maxTryToResolveCount, int maxResendOtpCount, int resendIntervalSeconds, String errorMessageCode) {
        super(name, messageCreator, messageSender, otpGenerator, maxTryToResolveCount, maxResendOtpCount, resendIntervalSeconds, errorMessageCode);
    }
}
