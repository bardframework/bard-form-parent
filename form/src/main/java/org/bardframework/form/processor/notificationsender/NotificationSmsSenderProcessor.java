package org.bardframework.form.processor.notificationsender;

import org.apache.commons.lang3.StringUtils;
import org.bardframework.commons.sms.SmsSender;
import org.bardframework.form.exception.FlowExecutionException;
import org.bardframework.form.field.base.FieldTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import java.util.List;
import java.util.Map;

public class NotificationSmsSenderProcessor extends NotificationSenderProcessor {

    protected final FieldTemplate<?> mobileNumberFieldTemplate;
    protected final SmsSender smsSender;

    public NotificationSmsSenderProcessor(String messageTemplateKey, String errorMessageCode, FieldTemplate<?> mobileNumberFieldTemplate, SmsSender smsSender, @Autowired MessageSource messageSource) {
        super(messageTemplateKey, errorMessageCode, messageSource);
        this.mobileNumberFieldTemplate = mobileNumberFieldTemplate;
        this.smsSender = smsSender;
    }

    @Override
    protected void send(Map<String, String> flowData, String message) {
        String mobileNumber = this.getMobileNumber(flowData);
        LOGGER.debug("sending message [{}]", message);
        boolean sendResult = this.getSmsSender().send(mobileNumber, message, flowData);
        if (!sendResult) {
            LOGGER.error("error sending sms to [{}]", mobileNumber);
            throw new FlowExecutionException(List.of(this.getErrorMessageCode()));
        }
        this.afterSend(flowData);
    }

    protected void afterSend(Map<String, String> flowData) {

    }

    protected String getMobileNumber(Map<String, String> flowData) {
        String mobileNumber = flowData.get(this.getMobileNumberFieldTemplate().getName());
        if (StringUtils.isBlank(mobileNumber)) {
            LOGGER.warn("mobile number not exist for [{}], can't send sms", flowData);
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
