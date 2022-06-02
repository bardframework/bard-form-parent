package org.bardframework.form.table.header;

import org.bardframework.form.common.table.TableHeader;

public class StringHeaderTemplate extends TableHeaderTemplate<TableHeader, String> {
    @Override
    public String toValue(String value) {
        return value;
    }

    @Override
    public String format(String value) {
        return value;
    }
}
