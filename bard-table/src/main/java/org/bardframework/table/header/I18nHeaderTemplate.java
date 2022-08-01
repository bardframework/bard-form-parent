package org.bardframework.table.header;

import org.springframework.context.MessageSource;

import java.util.Locale;

public class I18nHeaderTemplate extends StringHeaderTemplate {
    private String keyPrefix = "";

    public I18nHeaderTemplate() {
    }

    public Object format(String value, Locale locale, MessageSource messageSource) {
        return messageSource.getMessage(keyPrefix + value, new Object[0], value, locale);
    }

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }
}