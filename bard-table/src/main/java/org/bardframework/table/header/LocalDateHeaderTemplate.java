package org.bardframework.table.header;

import org.bardframework.commons.Calendar;
import org.bardframework.time.LocalDateJalali;
import org.springframework.context.MessageSource;

import java.time.LocalDate;
import java.time.chrono.HijrahDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;
import java.util.function.Supplier;

public class LocalDateHeaderTemplate extends HeaderTemplate<DateHeader, LocalDate> {

    private final DateTimeFormatter formatter;
    private DateTimeFormatter exportFormatter;
    private Supplier<Calendar> currentUserCalendarProvider = () -> Calendar.JALALI;

    public LocalDateHeaderTemplate(String formatterPattern) {
        this.formatter = DateTimeFormatter.ofPattern(formatterPattern);
        this.exportFormatter = DateTimeFormatter.ofPattern(formatterPattern);
    }

    @Override
    public Object format(LocalDate value, Locale locale, MessageSource messageSource) {
        if (null == value) {
            return null;
        }
        return this.getFormatter().withLocale(locale).format(value);
    }

    @Override
    public Object formatForExport(LocalDate value, Locale locale, MessageSource messageSource) {
        if (null == value) {
            return null;
        }
        Calendar calendar = currentUserCalendarProvider.get();
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

    public Supplier<Calendar> getCurrentUserCalendarProvider() {
        return currentUserCalendarProvider;
    }

    public void setCurrentUserCalendarProvider(Supplier<Calendar> currentUserCalendarProvider) {
        this.currentUserCalendarProvider = currentUserCalendarProvider;
    }

    public DateTimeFormatter getExportFormatter() {
        return exportFormatter;
    }

    public void setExportFormatter(DateTimeFormatter exportFormatter) {
        this.exportFormatter = exportFormatter;
    }

    public DateTimeFormatter getFormatter() {
        return formatter;
    }
}
