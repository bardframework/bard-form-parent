package org.bardframework.table.header;

public class ShortHeaderTemplate<M> extends HeaderTemplate<M, NumberHeader, Short> {

    @Override
    public NumberHeader getEmptyHeader() {
        return new NumberHeader();
    }
}
