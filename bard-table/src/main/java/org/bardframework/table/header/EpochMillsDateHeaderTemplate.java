package org.bardframework.table.header;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.bardframework.commons.utils.DateTimeUtils;
import org.bardframework.commons.web.Calendar;
import org.bardframework.time.LocalDateTimeJalali;
import org.springframework.context.MessageSource;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.chrono.HijrahDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;
import java.util.function.Supplier;

@Slf4j
@Getter
@Setter
@ToString
public class EpochMillsDateHeaderTemplate<M> extends HeaderTemplate<M, DateHeader, Long> {

    private DateTimeFormatter exportFormatter;
    private Supplier<ZoneId> timeZoneProvider = ZoneId::systemDefault;
    private Supplier<Calendar> calendarProvider = () -> Calendar.GREGORIAN;

    public EpochMillsDateHeaderTemplate(String exportFormatterPattern) {
        this(() -> exportFormatterPattern);
    }

    public EpochMillsDateHeaderTemplate(Supplier<String> exportFormatterPatternSupplier) {
        this.exportFormatter = DateTimeFormatter.ofPattern(exportFormatterPatternSupplier.get());
    }

    public EpochMillsDateHeaderTemplate(DateTimeFormatter exportFormatter) {
        this.exportFormatter = exportFormatter;
    }

    @Override
    public DateHeader getEmptyHeader() {
        return new DateHeader();
    }

    @Override
    public Object formatForExport(Long value, MessageSource messageSource, Locale locale) {
        LocalDateTime dateTime = DateTimeUtils.fromEpochMills(value, timeZoneProvider.get());
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
