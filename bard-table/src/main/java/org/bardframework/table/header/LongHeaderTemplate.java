package org.bardframework.table.header;

import org.springframework.context.MessageSource;

import java.util.Locale;

public class LongHeaderTemplate extends HeaderTemplate<NumberHeader, Long> {

    @Override
    public String format(Long value, Locale locale, MessageSource messageSource) {
        if (null == value) {
            return null;
        }
        return value.toString();
    }
}
