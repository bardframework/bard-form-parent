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
public class LocalTimeHeaderTemplate<M> extends HeaderTemplate<M, TimeHeader, LocalTime> {

    private final DateTimeFormatter formatter;

    public LocalTimeHeaderTemplate(String formatterPattern) {
        this.formatter = DateTimeFormatter.ofPattern(formatterPattern);
    }

    @Override
    public TimeHeader getEmptyHeader() {
        return new TimeHeader();
    }

    @Override
    public Object format(LocalTime value, MessageSource messageSource, Locale locale) {
        return this.getFormatter().withLocale(locale).format(value);
    }
}
