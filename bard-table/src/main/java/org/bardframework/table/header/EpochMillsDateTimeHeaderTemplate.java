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
public class EpochMillsDateTimeHeaderTemplate<M> extends HeaderTemplate<M, DateTimeHeader, Long> {

    private DateTimeFormatter exportFormatter;
    private Supplier<ZoneId> timeZoneProvider = ZoneId::systemDefault;
    private Supplier<Calendar> calendarProvider = () -> Calendar.GREGORIAN;

    public EpochMillsDateTimeHeaderTemplate(String exportFormatterPattern) {
        this(() -> exportFormatterPattern);
    }

    public EpochMillsDateTimeHeaderTemplate(Supplier<String> exportFormatterPatternSupplier) {
        this.exportFormatter = DateTimeFormatter.ofPattern(exportFormatterPatternSupplier.get());
    }

    public EpochMillsDateTimeHeaderTemplate(DateTimeFormatter exportFormatter) {
        this.exportFormatter = exportFormatter;
    }

    @Override
    public DateTimeHeader getEmptyHeader() {
        return new DateTimeHeader();
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
