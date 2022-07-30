package org.bardframework.table.header;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LocalDateHeaderTemplate extends HeaderTemplate<DateHeader, LocalDate> {

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
    public Object format(LocalDate value, Locale locale, MessageSource messageSource) {
        if (null == value) {
            return null;
        }
        return this.getFormatter().withLocale(locale).format(value);
    }

    public DateTimeFormatter getFormatter() {
        return formatter;
    }
}
