package org.bardframework.flow.form.field.input.otp.email;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.bardframework.flow.form.field.input.otp.OtpFieldTemplate;
import org.bardframework.flow.form.field.input.otp.OtpGenerator;
import org.bardframework.flow.processor.message.sender.MessageSender;
import org.bardframework.form.field.input.OtpField;

import java.util.Locale;
import java.util.Map;

@Slf4j
public class EotpFieldTemplate extends OtpFieldTemplate<OtpField, String> {

    private static final String ANSWER_KEY = "X_GENERATED_EOTP";
    private final MessageSender messageSender;

    public EotpFieldTemplate(String name, OtpGenerator<String> otpGenerator, int maxTryToResolveCount, MessageSender messageSender) {
        super(name, otpGenerator, maxTryToResolveCount);
        this.messageSender = messageSender;
    }

    @Override
    public void preProcess(String flowToken, Map<String, Object> flowData, Locale locale, HttpServletResponse httpResponse) throws Exception {
        this.sendInternal(flowToken, flowData, locale, httpResponse);
    }

    @Override
    protected void send(String flowToken, Map<String, Object> flowData, String otp, Locale locale, HttpServletResponse httpResponse) throws Exception {
        flowData.put(ANSWER_KEY, otp);
        this.messageSender.send(flowData, locale);
    }

    @Override
    protected boolean isValidOtp(String flowToken, String otp, Map<String, Object> flowData) throws Exception {
        Object expectedAnswer = flowData.get(ANSWER_KEY);
        if (null == expectedAnswer) {
            log.debug("eotp answer in flow data is blank, flow token: [{}]", flowToken);
            return false;
        }
        return expectedAnswer.toString().equalsIgnoreCase(otp);
    }

    @Override
    protected String getOtpMaxTryToResolveCountErrorMessage() {
        return "eotp.error.max.resolve.exceeded";
    }

    @Override
    protected String getMaxSendCountErrorMessage() {
        return "eotp.error.max.send.exceeded";
    }

    @Override
    protected String getResendAction() {
        return "eotp-resend";
    }
}
