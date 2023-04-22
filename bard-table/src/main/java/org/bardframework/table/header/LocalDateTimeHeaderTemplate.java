package org.bardframework.table.header;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.bardframework.commons.web.Calendar;
import org.bardframework.time.LocalDateTimeJalali;
import org.springframework.context.MessageSource;

import java.time.LocalDateTime;
import java.time.chrono.HijrahDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;
import java.util.function.Supplier;

@Slf4j
@Getter
@Setter
@ToString
public class LocalDateTimeHeaderTemplate extends HeaderTemplate<DateTimeHeader, LocalDateTime> {

    private final DateTimeFormatter formatter;
    private DateTimeFormatter exportFormatter;
    private Supplier<Calendar> currentUserCalendarProvider = () -> Calendar.JALALI;

    public LocalDateTimeHeaderTemplate(String formatterPattern) {
        this.formatter = DateTimeFormatter.ofPattern(formatterPattern);
        this.exportFormatter = DateTimeFormatter.ofPattern(formatterPattern);
    }

    @Override
    public Object format(LocalDateTime value, Locale locale, MessageSource messageSource) {
        if (null == value) {
            return null;
        }
        return this.getFormatter().withLocale(locale).format(value);
    }

    @Override
    public Object formatForExport(LocalDateTime value, Locale locale, MessageSource messageSource) {
        if (null == value) {
            return null;
        }
        Calendar calendar = currentUserCalendarProvider.get();
        TemporalAccessor accessor;
        if (calendar == Calendar.GREGORIAN) {
            accessor = value;
        } else if (calendar == Calendar.JALALI) {
            accessor = LocalDateTimeJalali.of(value);
        } else if (calendar == Calendar.ISLAMIC) {
            HijrahDate hijrahDate = HijrahDate.from(value);
            accessor = LocalDateTime.of(hijrahDate.get(ChronoField.YEAR), hijrahDate.get(ChronoField.MONTH_OF_YEAR), hijrahDate.get(ChronoField.DAY_OF_MONTH), value.getHour(), value.getMinute(), value.getSecond());
        } else {
            throw new IllegalStateException("unhandled calendar: " + calendar);
        }
        return this.getFormatter().withLocale(locale).format(accessor);
    }
}
