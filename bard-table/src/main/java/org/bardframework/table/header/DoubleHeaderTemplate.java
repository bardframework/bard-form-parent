package org.bardframework.table.header;

import org.springframework.context.MessageSource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;

public class DoubleHeaderTemplate<M> extends HeaderTemplate<M, NumberHeader, Double> {
    private final int decimalPlaces;

    public DoubleHeaderTemplate(int decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }

    @Override
    public NumberHeader getEmptyHeader() {
        return new NumberHeader();
    }

    @Override
    protected Double format(Double value, MessageSource messageSource, Locale locale) {
        return BigDecimal.valueOf(value)
                .setScale(decimalPlaces, RoundingMode.DOWN)
                .doubleValue();
    }
}
