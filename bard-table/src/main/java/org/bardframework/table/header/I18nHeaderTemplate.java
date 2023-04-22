package org.bardframework.table.header;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;

import java.util.Locale;

@Slf4j
@Getter
@Setter
@ToString
public class I18nHeaderTemplate extends StringHeaderTemplate {
    private String keyPrefix = "";

    public I18nHeaderTemplate() {
    }

    public Object format(String value, Locale locale, MessageSource messageSource) {
        if (null == value) {
            return null;
        }
        return messageSource.getMessage(keyPrefix + value, new Object[0], value, locale);
    }
}