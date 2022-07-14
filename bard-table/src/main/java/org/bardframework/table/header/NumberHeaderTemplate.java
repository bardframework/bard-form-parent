package org.bardframework.table.header;

import org.bardframework.form.table.header.NumberHeader;

import java.util.Locale;

public class NumberHeaderTemplate extends TableHeaderTemplate<NumberHeader, String> {
    @Override
    public String parse(String value, Locale locale) {
        return value;
    }

    @Override
    public Object format(String value, Locale locale) {
        return value;
    }
}
