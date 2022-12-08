package org.bardframework.table.header;

import org.springframework.context.MessageSource;

import java.util.Locale;

public class BooleanHeaderTemplate extends HeaderTemplate<StringHeader, Boolean> {

    @Override
    public String format(Boolean value, Locale locale, MessageSource messageSource) {
        if (null == value) {
            return messageSource.getMessage("Boolean.null", new Object[0], "", locale);
        }
        return messageSource.getMessage("Boolean." + value, new Object[0], value.toString(), locale);
    }
}
