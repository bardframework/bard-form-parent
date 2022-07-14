package org.bardframework.flow.processor.messagesender.creator;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

public interface MessageCreator {

    String create(Map<String, String> args, Locale locale) throws IOException;

}
