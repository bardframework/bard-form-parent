package org.bardframework.flow.processor.message.creator;

import org.bardframework.commons.utils.StringTemplateUtils;
import org.springframework.context.MessageSource;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

public class MessageProviderMessageSource implements MessageProvider {

    protected final String messageTemplateKey;
    protected final MessageSource messageSource;

    public MessageProviderMessageSource(String messageTemplateKey, MessageSource messageSource) {
        this.messageTemplateKey = messageTemplateKey;
        this.messageSource = messageSource;
    }

    @Override
    public String create(Map<String, String> args, Locale locale) throws IOException {
        String messageKey = StringTemplateUtils.fillTemplate(this.getMessageTemplateKey(), args);
        return this.getMessageSource().getMessage(messageKey, new Object[]{}, locale);
    }

    public MessageSource getMessageSource() {
        return messageSource;
    }

    public String getMessageTemplateKey() {
        return messageTemplateKey;
    }
}
