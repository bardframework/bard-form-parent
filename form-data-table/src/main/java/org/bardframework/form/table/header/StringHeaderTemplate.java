package org.bardframework.form.table.header;

import org.bardframework.form.table.TableHeader;

import java.util.Locale;

public class StringHeaderTemplate extends TableHeaderTemplate<TableHeader, String> {
    @Override
    public String parse(String value, Locale locale) {
        return value;
    }

    @Override
    public Object format(String value, Locale locale) {
        return value;
    }
}
