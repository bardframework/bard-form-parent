package org.bardframework.table.header;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.commons.lang3.math.NumberUtils;
import org.bardframework.commons.utils.ReflectionUtils;
import org.springframework.context.MessageSource;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Getter
@Setter
public class DurationPropertyPathHeaderTemplate extends HeaderTemplate<Object, StringHeader, String> {

    private final String propertyPath;
    private ChronoUnit accuracy = ChronoUnit.MILLIS;

    public DurationPropertyPathHeaderTemplate(String propertyPath) {
        this.propertyPath = propertyPath;
    }

    @SneakyThrows
    @Override
    public Object getValue(Object object, MessageSource messageSource, Locale locale, boolean export) {
        Object value = ReflectionUtils.getPropertyValue(object, propertyPath);
        if (value == null) {
            return null;
        }
        long micros = NumberUtils.toLong(value.toString());
        if (micros < 1000) {
            if (micros == 0) {
                return 0;
            }
            if (accuracy == ChronoUnit.MICROS) {
                return micros + "µs";
            } else {
                return 0;
            }
        }

        long millis = micros / 1000;
        long microsRemainder = micros % 1000;

        long seconds = millis / 1000;
        long millisRemainder = millis % 1000;

        long minutes = seconds / 60;
        long secondsRemainder = seconds % 60;

        long hours = minutes / 60;
        long minutesRemainder = minutes % 60;

        long days = hours / 24;
        long hoursRemainder = hours % 24;

        List<String> parts = new ArrayList<>();
        if (days > 0) {
            parts.add(days + "d");
        }
        if (hoursRemainder > 0) {
            parts.add(hoursRemainder + "h");
        }
        if (minutesRemainder > 0) {
            parts.add(minutesRemainder + "m");
        }
        if (secondsRemainder > 0) {
            parts.add(secondsRemainder + "s");
        }
        if (millisRemainder > 0) {
            parts.add(millisRemainder + "ms");
        }
        if (accuracy == ChronoUnit.MICROS && microsRemainder > 0) {
            parts.add(microsRemainder + "µs");
        }
        return String.join(" ", parts);
    }

    @Override
    public StringHeader getEmptyHeader() {
        return new StringHeader();
    }
}
