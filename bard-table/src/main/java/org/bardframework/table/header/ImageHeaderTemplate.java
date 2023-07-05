package org.bardframework.table.header;

public class ImageHeaderTemplate<M> extends HeaderTemplate<M, ImageHeader, String> {

    @Override
    public ImageHeader getEmptyHeader() {
        return new ImageHeader();
    }
}
