package org.bardframework.flow.processor.message.sender;

import java.util.Locale;
import java.util.Map;

public interface MessageSender {

    void send(Map<String, String> args, Locale locale) throws Exception;
}
