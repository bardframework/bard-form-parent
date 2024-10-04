package org.bardframework.table.header;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.bardframework.commons.web.Calendar;
import org.bardframework.time.LocalDateTimeJalali;
import org.springframework.context.MessageSource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
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
public class EpochMillsTimeHeaderTemplate<M> extends HeaderTemplate<M, TimeHeader, LocalTime> {

    private DateTimeFormatter exportFormatter;
    private Supplier<ZoneId> timeZoneProvider = ZoneId::systemDefault;
    private Supplier<Calendar> calendarProvider = () -> Calendar.GREGORIAN;

    public EpochMillsTimeHeaderTemplate(String exportFormatterPattern) {
        this(() -> exportFormatterPattern);
    }

    public EpochMillsTimeHeaderTemplate(Supplier<String> exportFormatterPatternSupplier) {
        this.exportFormatter = DateTimeFormatter.ofPattern(exportFormatterPatternSupplier.get());
    }

    public EpochMillsTimeHeaderTemplate(DateTimeFormatter exportFormatter) {
        this.exportFormatter = exportFormatter;
    }

    @Override
    public TimeHeader getEmptyHeader() {
        return new TimeHeader();
    }

    @Override
    protected Object format(LocalTime value, MessageSource messageSource, Locale locale) {
        return value.getLong(ChronoField.MILLI_OF_DAY);
    }

    @Override
    public Object formatForExport(LocalTime value, MessageSource messageSource, Locale locale) {
        LocalDateTime dateTime = LocalDateTime.of(LocalDate.MIN, value);
        Calendar calendar = calendarProvider.get();
        TemporalAccessor accessor;
        if (calendar == Calendar.GREGORIAN) {
            accessor = dateTime;
        } else if (calendar == Calendar.JALALI) {
            accessor = LocalDateTimeJalali.of(dateTime);
        } else if (calendar == Calendar.ISLAMIC) {
            accessor = HijrahDate.from(dateTime);
        } else {
            throw new IllegalStateException("unhandled calendar: " + calendar);
        }
        return this.getExportFormatter().withLocale(locale).format(accessor);
    }
}
