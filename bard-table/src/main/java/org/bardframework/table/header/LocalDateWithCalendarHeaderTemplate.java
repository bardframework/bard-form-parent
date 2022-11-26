package org.bardframework.table.header;

import org.bardframework.commons.Calendar;
import org.bardframework.time.LocalDateJalali;
import org.springframework.context.MessageSource;

import java.time.LocalDate;
import java.time.chrono.HijrahDate;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;

public abstract class LocalDateWithCalendarHeaderTemplate extends LocalDateHeaderTemplate {

    public LocalDateWithCalendarHeaderTemplate(String formatterPattern) {
        super(formatterPattern);
    }

    @Override
    public Object format(LocalDate value, Locale locale, MessageSource messageSource) {
        if (null == value) {
            return null;
        }
        Calendar calendar = this.getCurrentUserCalendar();
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

    protected abstract Calendar getCurrentUserCalendar();
}
