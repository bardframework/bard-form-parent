package org.bardframework.flow.processor.message.sender;

import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public interface MessageSender {

    void send(Map<String, String> args, Locale locale) throws Exception;

    Pattern canSendRegex();

    default boolean canSend(String receiver, Map<String, String> args) {
        return this.canSendRegex().matcher(receiver).matches();
    }

}
