package org.bardframework.table.header;

import lombok.SneakyThrows;
import org.bardframework.commons.utils.ReflectionUtils;
import org.springframework.context.MessageSource;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateTimePropertyPathHeaderTemplate extends HeaderTemplate<Object, StringHeader, String> {

    private final String propertyPath;
    private final DateTimeFormatter formatter;

    public DateTimePropertyPathHeaderTemplate(String propertyPath) {
        this.propertyPath = propertyPath;
        this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.US);
    }

    @SneakyThrows
    @Override
    public Object getValue(Object object, MessageSource messageSource, Locale locale, boolean export) {
        Object value = ReflectionUtils.getPropertyValue(object, propertyPath);
        if (value == null) {
            return null;
        }
        ZonedDateTime zonedDateTime;
        switch (value) {
            case Long longValue -> {
                Instant instant = Instant.ofEpochMilli(longValue);
                zonedDateTime = instant.atZone(ZoneId.systemDefault());
            }
            case LocalDateTime localDateTime -> zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
            case String stringValue -> zonedDateTime = ZonedDateTime.parse(stringValue);
            default ->
                    throw new IllegalArgumentException(String.format("value [%s] of type [%s] is not supported as date time.", value, value.getClass()));
        }
        return formatter.format(zonedDateTime);
    }

    @Override
    public StringHeader getEmptyHeader() {
        return new StringHeader();
    }
}
