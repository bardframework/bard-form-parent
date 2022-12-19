package org.bardframework.table.header;

import org.springframework.context.MessageSource;

import java.util.Locale;

public class NumberHeaderTemplate extends HeaderTemplate<NumberHeader, String> {

    @Override
    public Object format(String value, Locale locale, MessageSource messageSource) {
        return value;
    }
}
