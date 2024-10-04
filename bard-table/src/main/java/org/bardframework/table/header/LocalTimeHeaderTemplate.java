package org.bardframework.table.header;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.function.Supplier;

@Slf4j
@Getter
@ToString
public class LocalTimeHeaderTemplate<M> extends HeaderTemplate<M, TimeHeader, LocalTime> {

    private final DateTimeFormatter formatter;

    public LocalTimeHeaderTemplate(String exportFormatterPattern) {
        this(() -> exportFormatterPattern);
    }

    public LocalTimeHeaderTemplate(Supplier<String> exportFormatterPatternSupplier) {
        this.formatter = DateTimeFormatter.ofPattern(exportFormatterPatternSupplier.get());
    }

    public LocalTimeHeaderTemplate(DateTimeFormatter exportFormatter) {
        this.formatter = exportFormatter;
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
