package org.bardframework.table.header;

import org.springframework.context.MessageSource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;

public class DoubleHeaderTemplate extends HeaderTemplate<NumberHeader, Double> {
    private final int decimalPlaces;

    public DoubleHeaderTemplate(int decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }

    public String format(Double value, Locale locale, MessageSource messageSource) {
        return BigDecimal.valueOf(value)
                .setScale(decimalPlaces, RoundingMode.DOWN)
                .toString();
    }
}
