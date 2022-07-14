package org.bardframework.flow.processor.messagesender.sender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.Map;

public class MessageSenderNoOp implements MessageSender {
    protected static final Logger LOGGER = LoggerFactory.getLogger(MessageSenderNoOp.class);

    @Override
    public void send(String message, Map<String, String> args, Locale locale) {
        LOGGER.debug("noop: send message [{}]", message);
    }
}
