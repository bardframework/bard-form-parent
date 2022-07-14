package org.bardframework.table.header;

import java.util.Locale;

public class StringHeaderTemplate extends TableHeaderTemplate<StringHeader, String> {
    @Override
    public String parse(String value, Locale locale) {
        return value;
    }

    @Override
    public Object format(String value, Locale locale) {
        return value;
    }
}
