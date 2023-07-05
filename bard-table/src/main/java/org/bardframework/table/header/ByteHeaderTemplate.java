package org.bardframework.table.header;

public class ByteHeaderTemplate<M> extends HeaderTemplate<M, NumberHeader, Byte> {

    @Override
    public NumberHeader getEmptyHeader() {
        return new NumberHeader();
    }
}
