package org.bardframework.table.header;

import org.springframework.context.MessageSource;

import java.util.Locale;

public class LongHeaderTemplate extends HeaderTemplate<StringHeader, Long> {

    @Override
    public String format(Long value, Locale locale, MessageSource messageSource) {
        return null == value ? null : value.toString();
    }
}
