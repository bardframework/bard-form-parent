package org.bardframework.form.table.header;

import org.apache.commons.lang3.StringUtils;
import org.bardframework.form.common.table.TableHeader;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalTimeHeaderTemplate extends TableHeaderTemplate<TableHeader, LocalTime> {

    private final DateTimeFormatter formatter;

    public LocalTimeHeaderTemplate(String formatterPattern) {
        this.formatter = DateTimeFormatter.ofPattern(formatterPattern);
    }

    @Override
    public LocalTime toValue(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return LocalTime.parse(value);
    }

    @Override
    public Object format(LocalTime value) {
        if (null == value) {
            return null;
        }
        return formatter.format(value);
    }
}
