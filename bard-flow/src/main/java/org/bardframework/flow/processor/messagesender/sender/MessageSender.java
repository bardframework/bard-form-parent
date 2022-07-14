package org.bardframework.flow.processor.messagesender.sender;

import java.util.Locale;
import java.util.Map;

public interface MessageSender {

    void send(String message, Map<String, String> args, Locale locale) throws Exception;

}
