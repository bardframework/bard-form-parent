package org.bardframework.table.header;

import org.bardframework.table.header.type.HeaderType;
import org.bardframework.table.header.type.TableHeaderType;

public class Ip4Header extends TableHeader {
    @Override
    public HeaderType getType() {
        return TableHeaderType.IP4;
    }
}
