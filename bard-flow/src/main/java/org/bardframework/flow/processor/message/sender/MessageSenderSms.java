package org.bardframework.flow.processor.message.sender;

import org.apache.commons.lang3.StringUtils;
import org.bardframework.commons.sms.SmsSender;
import org.bardframework.flow.processor.message.creator.MessageProvider;
import org.bardframework.form.field.FieldTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

public class MessageSenderSms extends MessageSenderAbstract {
    protected static final Logger LOGGER = LoggerFactory.getLogger(MessageSenderSms.class);

    protected final SmsSender smsSender;
    protected final FieldTemplate<?> mobileNumberFieldTemplate;

    public MessageSenderSms(SmsSender smsSender, MessageProvider messageProvider, String errorMessageKey, FieldTemplate<?> mobileNumberFieldTemplate) {
        super(messageProvider, errorMessageKey);
        this.smsSender = smsSender;
        this.mobileNumberFieldTemplate = mobileNumberFieldTemplate;
    }

    @Override
    protected void send(String message, Map<String, String> args, Locale locale) throws IOException {
        String mobileNumber = args.get(this.getMobileNumberFieldTemplate().getName());
        if (StringUtils.isBlank(mobileNumber)) {
            LOGGER.warn("mobile number not exist for [{}], can't send sms", args);
            throw new IllegalStateException("mobile number not exist in args");
        }
        LOGGER.debug("sending message [{}]", message);
        boolean sendResult = this.getSmsSender().send(mobileNumber, message, args);
        if (!sendResult) {
            LOGGER.error("error sending sms to [{}]", mobileNumber);
            throw new IllegalStateException("error sending sms to: " + mobileNumber);
        }
    }

    public SmsSender getSmsSender() {
        return smsSender;
    }

    public FieldTemplate<?> getMobileNumberFieldTemplate() {
        return mobileNumberFieldTemplate;
    }
}
