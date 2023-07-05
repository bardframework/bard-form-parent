package org.bardframework.table.header;

public class NumberHeaderTemplate<M> extends HeaderTemplate<M, NumberHeader, String> {

    @Override
    public NumberHeader getEmptyHeader() {
        return new NumberHeader();
    }
}
