package org.bardframework.table.header;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.bardframework.commons.web.Calendar;
import org.bardframework.time.LocalDateJalali;
import org.springframework.context.MessageSource;

import java.time.LocalDate;
import java.time.chrono.HijrahDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;
import java.util.function.Supplier;

@Slf4j
@Getter
@Setter
@ToString
public class LocalDateHeaderTemplate<M> extends HeaderTemplate<M, DateHeader, LocalDate> {

    private final DateTimeFormatter formatter;
    private DateTimeFormatter exportFormatter;
    private Supplier<Calendar> calendarProvider = () -> Calendar.GREGORIAN;

    public LocalDateHeaderTemplate(String formatterPattern) {
        this.formatter = DateTimeFormatter.ofPattern(formatterPattern);
        this.exportFormatter = DateTimeFormatter.ofPattern(formatterPattern);
    }

    @Override
    public Object format(LocalDate value, MessageSource messageSource, Locale locale) {
        return this.getFormatter().withLocale(locale).format(value);
    }

    @Override
    public Object formatForExport(LocalDate value, MessageSource messageSource, Locale locale) {
        Calendar calendar = calendarProvider.get();
        TemporalAccessor accessor;
        if (calendar == Calendar.GREGORIAN) {
            accessor = value;
        } else if (calendar == Calendar.JALALI) {
            accessor = LocalDateJalali.of(value);
        } else if (calendar == Calendar.ISLAMIC) {
            accessor = HijrahDate.from(value);
        } else {
            throw new IllegalStateException("unhandled calendar: " + calendar);
        }
        return this.getFormatter().withLocale(locale).format(accessor);
    }
}
