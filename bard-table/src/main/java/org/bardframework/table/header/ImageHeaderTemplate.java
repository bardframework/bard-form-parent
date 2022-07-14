package org.bardframework.table.header;

import java.util.Locale;

public class ImageHeaderTemplate extends TableHeaderTemplate<ImageHeader, String> {
    @Override
    public String parse(String value, Locale locale) {
        return value;
    }

    @Override
    public Object format(String value, Locale locale) {
        return value;
    }
}
