package org.bardframework.flow.processor.message.sender;

import org.bardframework.flow.processor.message.creator.MessageProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

public class MessageSenderNoOp extends MessageSenderAbstract {

    protected static final Logger log = LoggerFactory.getLogger(MessageSenderNoOp.class);

    public MessageSenderNoOp(String receiverFieldName, MessageProvider messageProvider) {
        super(receiverFieldName, messageProvider, "no op message sender error");
    }

    @Override
    protected void send(String receiver, String message, Map<String, String> args, Locale locale) throws IOException {
        log.info("send message [{}] to [{}]", message, receiver);
    }
}
