package org.bardframework.flow.form.field.input.otp.email;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.bardframework.flow.form.field.input.otp.OtpFieldTemplate;
import org.bardframework.flow.form.field.input.otp.OtpGenerator;
import org.bardframework.flow.processor.message.sender.MessageSenderEmail;
import org.bardframework.form.field.input.OtpField;

import java.util.Locale;
import java.util.Map;

public class EotpFieldTemplate extends OtpFieldTemplate<OtpField, String> {

    private static final String ANSWER_KEY = "X_GENERATED_EOTP";
    private final MessageSenderEmail messageSender;

    public EotpFieldTemplate(String name, OtpGenerator<String> otpGenerator, int maxTryToResolveCount, MessageSenderEmail messageSender) {
        super(name, otpGenerator, maxTryToResolveCount);
        this.messageSender = messageSender;
    }

    @Override
    public void preProcess(String flowToken, Map<String, String> flowData, Locale locale, HttpServletResponse httpResponse) throws Exception {
        this.sendInternal(flowToken, flowData, locale, httpResponse);
    }

    @Override
    protected void send(String flowToken, Map<String, String> flowData, String otp, Locale locale, HttpServletResponse httpResponse) throws Exception {
        flowData.put(ANSWER_KEY, otp);
        this.messageSender.send(flowData, locale);
    }

    @Override
    protected boolean isValidOtp(String flowToken, String otp, Map<String, String> flowData) throws Exception {
        String expectedAnswer = flowData.get(ANSWER_KEY);
        if (StringUtils.isBlank(expectedAnswer)) {
            LOGGER.debug("eotp answer in flow data is blank, flow token: [{}]", flowToken);
            return false;
        }
        return expectedAnswer.equalsIgnoreCase(otp);
    }

    @Override
    protected String getResendAction() {
        return "eotp-resend";
    }
}
