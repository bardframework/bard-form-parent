package org.bardframework.form.table.header;

import org.apache.commons.lang3.StringUtils;
import org.bardframework.form.common.table.TableHeader;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateHeaderTemplate extends TableHeaderTemplate<TableHeader, LocalDate> {

    private final DateTimeFormatter formatter;

    public LocalDateHeaderTemplate(String formatterPattern) {
        this.formatter = DateTimeFormatter.ofPattern(formatterPattern);
    }

    @Override
    public LocalDate toValue(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return LocalDate.parse(value);
    }

    @Override
    public String format(LocalDate value) {
        if (null == value) {
            return null;
        }
        return formatter.format(value);
    }
}
