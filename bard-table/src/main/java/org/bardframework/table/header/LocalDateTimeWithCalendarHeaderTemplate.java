package org.bardframework.table.header;

import org.bardframework.commons.Calendar;
import org.bardframework.time.LocalDateTimeJalali;
import org.springframework.context.MessageSource;

import java.time.LocalDateTime;
import java.time.chrono.HijrahDate;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;

public abstract class LocalDateTimeWithCalendarHeaderTemplate extends LocalDateTimeHeaderTemplate {
    public LocalDateTimeWithCalendarHeaderTemplate(String formatterPattern) {
        super(formatterPattern);
    }

    @Override
    public Object format(LocalDateTime value, Locale locale, MessageSource messageSource) {
        if (null == value) {
            return null;
        }
        Calendar calendar = this.getCurrentUserCalendar();
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

    protected abstract Calendar getCurrentUserCalendar();
}
