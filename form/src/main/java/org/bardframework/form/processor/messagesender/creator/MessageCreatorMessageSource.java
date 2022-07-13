package org.bardframework.form.processor.messagesender.creator;

import org.springframework.context.MessageSource;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

public class MessageCreatorMessageSource implements MessageCreator {

    protected final String messageTemplateKey;
    protected final MessageSource messageSource;

    public MessageCreatorMessageSource(String messageTemplateKey, MessageSource messageSource) {
        this.messageTemplateKey = messageTemplateKey;
        this.messageSource = messageSource;
    }

    @Override
    public String create(Map<String, String> args, Locale locale) throws IOException {
        return this.getMessageSource().getMessage(this.getMessageTemplateKey(), new Object[]{}, locale);
    }

    public MessageSource getMessageSource() {
        return messageSource;
    }

    public String getMessageTemplateKey() {
        return messageTemplateKey;
    }
}
