package org.bardframework.table.header;

import org.springframework.context.MessageSource;

import java.util.Locale;

public class ImageHeaderTemplate extends HeaderTemplate<ImageHeader, String> {

    @Override
    public Object format(String value, Locale locale, MessageSource messageSource) {
        return value;
    }
}
