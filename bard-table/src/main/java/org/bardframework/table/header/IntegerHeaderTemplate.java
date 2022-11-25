package org.bardframework.table.header;

import org.springframework.context.MessageSource;

import java.util.Locale;

public class IntegerHeaderTemplate extends HeaderTemplate<StringHeader, Integer> {

    @Override
    public String format(Integer value, Locale locale, MessageSource messageSource) {
        if (null == value) {
            return null;
        }
        return value.toString();
    }
}
