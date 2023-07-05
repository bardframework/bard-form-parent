package org.bardframework.table.header;

public class StringHeaderTemplate<M> extends HeaderTemplate<M, StringHeader, String> {

    @Override
    public StringHeader getEmptyHeader() {
        return new StringHeader();
    }
}
