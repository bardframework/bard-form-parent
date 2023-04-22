package org.bardframework.table.header;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Slf4j
@Getter
@ToString
public class LocalTimeHeaderTemplate extends HeaderTemplate<TimeHeader, LocalTime> {

    private final DateTimeFormatter formatter;

    public LocalTimeHeaderTemplate(String formatterPattern) {
        this.formatter = DateTimeFormatter.ofPattern(formatterPattern);
    }

    @Override
    public Object format(LocalTime value, Locale locale, MessageSource messageSource) {
        if (null == value) {
            return null;
        }
        return this.getFormatter().withLocale(locale).format(value);
    }
}
