package org.bardframework.table.header;

public class IntegerHeaderTemplate<M> extends HeaderTemplate<M, NumberHeader, Integer> {

    @Override
    public NumberHeader getEmptyHeader() {
        return new NumberHeader();
    }
}
