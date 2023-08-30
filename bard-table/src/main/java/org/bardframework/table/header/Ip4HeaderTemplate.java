package org.bardframework.table.header;

public class Ip4HeaderTemplate<M> extends HeaderTemplate<M, Ip4Header, String> {

    @Override
    public Ip4Header getEmptyHeader() {
        return new Ip4Header();
    }
}
