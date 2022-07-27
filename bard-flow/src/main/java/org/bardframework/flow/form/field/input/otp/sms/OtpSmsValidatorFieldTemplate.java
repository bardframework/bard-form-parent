package org.bardframework.flow.form.field.input.otp.sms;

import org.apache.commons.lang3.StringUtils;
import org.bardframework.flow.form.field.input.FlowInputFieldTemplate;
import org.bardframework.flow.processor.messagesender.creator.MessageCreator;
import org.bardframework.flow.processor.messagesender.otp.OtpGenerator;
import org.bardframework.flow.processor.messagesender.sender.MessageSenderSms;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;
import org.bardframework.form.field.input.TextField;
import org.springframework.context.MessageSource;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public class OtpSmsValidatorFieldTemplate extends FlowInputFieldTemplate<TextField, String> {

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

    @Override
    public void fill(FormTemplate formTemplate, TextField field, Map<String, String> values, Locale locale, HttpServletRequest httpRequest) throws Exception {
        super.fill(formTemplate, field, values, locale, httpRequest);
        field.setRegex(FormUtils.getFieldStringProperty(formTemplate, this, "regex", locale, values, null));
        field.setMask(FormUtils.getFieldStringProperty(formTemplate, this, "mask", locale, values, null));
        field.setMinLength(FormUtils.getFieldIntegerProperty(formTemplate, this, "minLength", locale, values, null));
        field.setMaxLength(FormUtils.getFieldIntegerProperty(formTemplate, this, "maxLength", locale, values, null));
    }

    @Override
    public boolean isValid(TextField field, String value, Map<String, String> flowData) {
        if (StringUtils.isBlank(value)) {
            if (Boolean.TRUE.equals(field.getRequired())) {
                LOGGER.debug("field [{}] is required, but it's value is empty", field.getName());
                return false;
            }
            return true;
        }
        if (null != field.getMinLength() && value.length() < field.getMinLength()) {
            LOGGER.debug("field [{}] min length is [{}], but it's value[{}] length is smaller than minimum", field.getName(), field.getMinLength(), value);
            return false;
        }
        if (null != field.getMaxLength() && value.length() > field.getMaxLength()) {
            LOGGER.debug("field [{}] max length is [{}], but it's value[{}] length is greater than maximum", field.getName(), field.getMaxLength(), value);
            return false;
        }
        if (StringUtils.isNotBlank(field.getRegex()) && !Pattern.matches(field.getRegex(), value)) {
            LOGGER.debug("field [{}] regex is [{}], but it's value[{}] not match with it", field.getName(), field.getRegex(), value);
            return false;
        }
        return true;
    }

    @Override
    public String toValue(String value) {
        return value;
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
