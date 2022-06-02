package org.bardframework.form.table.header;

import org.bardframework.form.common.table.TableHeader;

public class StringHeaderTemplate extends TableHeaderTemplate<TableHeader, String> {
    @Override
    public String toValue(String value) {
        return value;
    }

    @Override
    public Object format(String value) {
        return value;
    }
}
