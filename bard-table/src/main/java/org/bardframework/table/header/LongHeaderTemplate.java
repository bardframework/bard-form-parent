package org.bardframework.table.header;

public class LongHeaderTemplate<M> extends HeaderTemplate<M, NumberHeader, Long> {

    @Override
    public NumberHeader getEmptyHeader() {
        return new NumberHeader();
    }
}
