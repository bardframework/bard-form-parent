package org.bardframework.table.header;

public class AvatarHeaderTemplate<M> extends HeaderTemplate<M, AvatarHeader, String> {

    @Override
    public AvatarHeader getEmptyHeader() {
        return new AvatarHeader();
    }
}
