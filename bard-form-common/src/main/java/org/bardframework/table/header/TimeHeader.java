package org.bardframework.table.header;

import org.bardframework.table.header.type.HeaderType;
import org.bardframework.table.header.type.TableHeaderType;

public class TimeHeader extends TableHeader {
    @Override
    public HeaderType getType() {
        return TableHeaderType.TIME;
    }
}
