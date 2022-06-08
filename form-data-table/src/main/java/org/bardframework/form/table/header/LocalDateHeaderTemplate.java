package org.bardframework.form.table.header;

import org.apache.commons.lang3.StringUtils;
import org.bardframework.form.table.TableHeader;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LocalDateHeaderTemplate extends TableHeaderTemplate<TableHeader, LocalDate> {

    private final DateTimeFormatter formatter;

    public LocalDateHeaderTemplate(String formatterPattern) {
        this.formatter = DateTimeFormatter.ofPattern(formatterPattern);
    }

    @Override
    public LocalDate parse(String value, Locale locale) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return LocalDate.parse(value);
    }

    @Override
    public Object format(LocalDate value, Locale locale) {
        if (null == value) {
            return null;
        }
        return formatter.format(value);
    }
}
