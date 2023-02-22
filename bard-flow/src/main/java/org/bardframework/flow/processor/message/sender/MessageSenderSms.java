package org.bardframework.flow.processor.message.sender;

import org.bardframework.commons.sms.SmsSender;
import org.bardframework.flow.processor.message.creator.MessageProvider;
import org.bardframework.form.field.FieldTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

public class MessageSenderSms extends MessageSenderAbstract {
    protected static final Logger log = LoggerFactory.getLogger(MessageSenderSms.class);

    protected final SmsSender smsSender;

    public MessageSenderSms(FieldTemplate<?> receiverFieldTemplate, SmsSender smsSender, MessageProvider messageProvider, String errorMessageKey) {
        super(receiverFieldTemplate.getName(), messageProvider, errorMessageKey);
        this.smsSender = smsSender;
    }

    public MessageSenderSms(String receiverFieldName, SmsSender smsSender, MessageProvider messageProvider, String errorMessageKey) {
        super(receiverFieldName, messageProvider, errorMessageKey);
        this.smsSender = smsSender;
    }

    @Override
    protected void send(String receiver, String message, Map<String, String> args, Locale locale) throws IOException {
        log.debug("sending message [{}]", message);
        boolean sendResult = this.getSmsSender().send(receiver, message, args);
        if (!sendResult) {
            log.error("error sending sms to [{}]", receiver);
            throw new IllegalStateException("error sending sms to: " + receiver);
        }
    }

    public SmsSender getSmsSender() {
        return smsSender;
    }
}
