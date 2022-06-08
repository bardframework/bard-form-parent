package org.bardframework.form.processor.notificationsender;

import org.apache.commons.lang3.StringUtils;
import org.bardframework.commons.sms.SmsSender;
import org.bardframework.form.exception.FlowExecutionException;
import org.bardframework.form.field.FieldTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class NotificationSmsSenderProcessor extends NotificationSenderProcessor {

    protected final FieldTemplate<?> mobileNumberFieldTemplate;
    protected final SmsSender smsSender;

    public NotificationSmsSenderProcessor(String messageTemplateKey, String errorMessageCode, boolean failOnError, FieldTemplate<?> mobileNumberFieldTemplate, SmsSender smsSender, @Autowired MessageSource messageSource) {
        super(messageTemplateKey, errorMessageCode, failOnError, messageSource);
        this.mobileNumberFieldTemplate = mobileNumberFieldTemplate;
        this.smsSender = smsSender;
    }

    @Override
    protected void send(String message, Map<String, String> args) throws IOException {
        String mobileNumber = this.getMobileNumber(args);
        LOGGER.debug("sending message [{}]", message);
        boolean sendResult = this.getSmsSender().send(mobileNumber, message, args);
        if (!sendResult) {
            LOGGER.error("error sending sms to [{}]", mobileNumber);
            throw new FlowExecutionException(List.of(this.getErrorMessageCode()));
        }
    }

    protected String getMobileNumber(Map<String, String> args) {
        String mobileNumber = args.get(this.getMobileNumberFieldTemplate().getName());
        if (StringUtils.isBlank(mobileNumber)) {
            LOGGER.warn("mobile number not exist for [{}], can't send sms", args);
            throw new FlowExecutionException(List.of(this.getErrorMessageCode()));
        }
        return mobileNumber;
    }

    public FieldTemplate<?> getMobileNumberFieldTemplate() {
        return mobileNumberFieldTemplate;
    }

    public SmsSender getSmsSender() {
        return smsSender;
    }
}
