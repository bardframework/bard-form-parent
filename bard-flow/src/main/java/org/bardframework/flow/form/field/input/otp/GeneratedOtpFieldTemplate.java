package org.bardframework.flow.form.field.input.otp;

import org.apache.commons.lang3.StringUtils;
import org.bardframework.flow.exception.InvalidateFlowException;
import org.bardframework.flow.processor.messagesender.creator.MessageCreator;
import org.bardframework.flow.processor.messagesender.otp.OtpGenerator;
import org.bardframework.flow.processor.messagesender.sender.MessageSender;

import java.util.List;
import java.util.Map;

public abstract class GeneratedOtpFieldTemplate extends OtpFieldTemplate {

    protected static final String RESOLVE_COUNT_KEY = "X_otp_resolve_count";
    protected final int maxTryToResolveCount;
    protected String resendAction;

    public GeneratedOtpFieldTemplate(String name, MessageCreator messageCreator, MessageSender messageSender, OtpGenerator otpGenerator, int maxTryToResolveCount, int maxResendOtpCount, int resendIntervalSeconds, String errorMessageCode) {
        super(name, otpGenerator.length());
        this.maxTryToResolveCount = maxTryToResolveCount;
        this.resendIntervalSeconds = resendIntervalSeconds;
        OtpSenderProcessor otpSenderProcessor = new OtpSenderProcessor(otpGenerator, messageCreator, messageSender, errorMessageCode);
        this.setPreProcessors(List.of(otpSenderProcessor));
        this.setResendAction("otp-resend");
        this.setActionProcessors(Map.of(this.getResendAction(), List.of(new OtpResendProcessor(otpSenderProcessor, maxResendOtpCount, resendIntervalSeconds))));
        this.setPersistentValue(false);
    }

    public String getResendAction() {
        return resendAction;
    }

    public void setResendAction(String resendAction) {
        this.resendAction = resendAction;
    }

    @Override
    protected boolean isValidOtp(String flowToken, String otp, Map<String, String> flowData) throws Exception {
        String generatedOtp = flowData.get(OtpSenderProcessor.GENERATED_OTP_KEY);
        if (StringUtils.isBlank(generatedOtp)) {
            throw new InvalidateFlowException(flowToken, "generated otp is empty, can't validate otp.");
        }
        if (generatedOtp.equalsIgnoreCase(otp.trim())) {
            flowData.remove(OtpSenderProcessor.GENERATED_OTP_KEY);
            flowData.remove(GeneratedOtpFieldTemplate.RESOLVE_COUNT_KEY);
            return true;
        }
        int resolveTryCount = flowData.containsKey(GeneratedOtpFieldTemplate.RESOLVE_COUNT_KEY) ? Integer.parseInt(flowData.get(GeneratedOtpFieldTemplate.RESOLVE_COUNT_KEY)) : 0;
        if (resolveTryCount >= maxTryToResolveCount) {
            throw new InvalidateFlowException(flowToken, "too many try to resolve otp, terminating flow...");
        }
        flowData.put(GeneratedOtpFieldTemplate.RESOLVE_COUNT_KEY, String.valueOf(resolveTryCount + 1));
        return false;
    }
}
