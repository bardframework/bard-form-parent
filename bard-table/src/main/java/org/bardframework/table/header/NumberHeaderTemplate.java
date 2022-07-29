package org.bardframework.table.header;

import org.springframework.context.MessageSource;

import java.util.Locale;

public class NumberHeaderTemplate extends HeaderTemplate<NumberHeader, String> {
    @Override
    public String parse(String value, Locale locale) {
        return value;
    }

    @Override
    public Object format(String value, Locale locale, MessageSource messageSource) {
        return value;
    }
}
