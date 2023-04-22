package org.bardframework.flow.processor.message.sender;

import lombok.extern.slf4j.Slf4j;
import org.bardframework.flow.processor.message.creator.MessageProvider;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

@Slf4j
public class MessageSenderNoOp extends MessageSenderAbstract {

    public MessageSenderNoOp(String receiverFieldName, MessageProvider messageProvider) {
        super(receiverFieldName, messageProvider, "no op message sender error");
    }

    @Override
    protected void send(String receiver, String message, Map<String, String> args, Locale locale) throws IOException {
        log.info("send message [{}] to [{}]", message, receiver);
    }
}
