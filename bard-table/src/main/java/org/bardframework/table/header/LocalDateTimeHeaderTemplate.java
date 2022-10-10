package org.bardframework.table.header;

import org.springframework.context.MessageSource;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LocalDateTimeHeaderTemplate extends HeaderTemplate<DateTimeHeader, LocalDateTime> {

    private final DateTimeFormatter formatter;

    public LocalDateTimeHeaderTemplate(String formatterPattern) {
        this.formatter = DateTimeFormatter.ofPattern(formatterPattern);
    }

    @Override
    public Object format(LocalDateTime value, Locale locale, MessageSource messageSource) {
        if (null == value) {
            return null;
        }
        return this.getFormatter().withLocale(locale).format(value);
    }

    public DateTimeFormatter getFormatter() {
        return formatter;
    }
}
