package org.bardframework.table.header;

import org.bardframework.form.table.header.ImageHeader;

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
