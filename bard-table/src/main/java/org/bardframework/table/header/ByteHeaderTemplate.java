package org.bardframework.table.header;

import org.springframework.context.MessageSource;

import java.util.Locale;

public class ByteHeaderTemplate extends HeaderTemplate<StringHeader, Byte> {

    @Override
    public String format(Byte value, Locale locale, MessageSource messageSource) {
        return null == value ? null : value.toString();
    }
}
