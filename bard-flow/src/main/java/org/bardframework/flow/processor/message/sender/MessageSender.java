package org.bardframework.flow.processor.message.sender;

import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public interface MessageSender {

    void send(Map<String, Object> args, Locale locale) throws Exception;

    Pattern canSendRegex();

    default boolean canSend(String receiver, Map<String, Object> args) {
        return null == this.canSendRegex() || this.canSendRegex().matcher(receiver).matches();
    }

}
