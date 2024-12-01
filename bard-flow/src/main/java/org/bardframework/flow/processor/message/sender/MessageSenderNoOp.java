package org.bardframework.flow.processor.message.sender;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

@Slf4j
public class MessageSenderNoOp extends MessageSenderAbstract {

    public MessageSenderNoOp() {
        super(null, null, null);
    }

    @Override
    protected void send(String receiver, String message, Map<String, Object> args, Locale locale) throws IOException {
        log.info("send message [{}] to [{}]", message, receiver);
    }
}
