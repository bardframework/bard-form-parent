package org.bardframework.table.header;

public class Ip6HeaderTemplate<M> extends HeaderTemplate<M, Ip6Header, String> {

    @Override
    public Ip6Header getEmptyHeader() {
        return new Ip6Header();
    }
}
