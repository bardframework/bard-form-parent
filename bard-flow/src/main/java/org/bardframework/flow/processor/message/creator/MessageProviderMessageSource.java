package org.bardframework.flow.processor.message.creator;

import lombok.Getter;
import org.bardframework.commons.utils.StringTemplateUtils;
import org.springframework.context.MessageSource;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

@Getter
public class MessageProviderMessageSource implements MessageProvider {

    protected final String messageTemplateKey;
    protected final MessageSource messageSource;

    public MessageProviderMessageSource(String messageTemplateKey, MessageSource messageSource) {
        this.messageTemplateKey = messageTemplateKey;
        this.messageSource = messageSource;
    }

    @Override
    public String create(Map<String, Object> args, Locale locale) throws IOException {
        String messageKey = StringTemplateUtils.fillTemplate(this.getMessageTemplateKey(), args);
        return this.getMessageSource().getMessage(messageKey, new Object[]{}, locale);
    }

}
