package org.bardframework.table.header;

import org.springframework.context.MessageSource;

import java.util.Locale;

public class I18nHeaderTemplate extends StringHeaderTemplate {
    public I18nHeaderTemplate() {
    }

    public Object format(String value, Locale locale, MessageSource messageSource) {
        return messageSource.getMessage(value, new Object[0], value, locale);
    }
}