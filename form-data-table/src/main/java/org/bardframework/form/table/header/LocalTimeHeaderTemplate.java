package org.bardframework.form.table.header;

import org.apache.commons.lang3.StringUtils;
import org.bardframework.form.common.table.TableHeader;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LocalTimeHeaderTemplate extends TableHeaderTemplate<TableHeader, LocalTime> {

    private final DateTimeFormatter formatter;

    public LocalTimeHeaderTemplate(String formatterPattern) {
        this.formatter = DateTimeFormatter.ofPattern(formatterPattern);
    }

    @Override
    public LocalTime parse(String value, Locale locale) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return LocalTime.parse(value);
    }

    @Override
    public Object format(LocalTime value, Locale locale) {
        if (null == value) {
            return null;
        }
        return formatter.format(value);
    }
}
