package org.bardframework.form.table.header;

import org.apache.commons.lang3.StringUtils;
import org.bardframework.form.common.table.TableHeader;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeHeaderTemplate extends TableHeaderTemplate<TableHeader, LocalDateTime> {

    private final DateTimeFormatter formatter;

    public LocalDateTimeHeaderTemplate(String formatterPattern) {
        this.formatter = DateTimeFormatter.ofPattern(formatterPattern);
    }

    @Override
    public LocalDateTime toValue(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return LocalDateTime.parse(value);
    }

    @Override
    public Object format(LocalDateTime value) {
        if (null == value) {
            return null;
        }
        return formatter.format(value);
    }
}
