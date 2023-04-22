package org.bardframework.form.field.filter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.bardframework.form.field.FieldType;
import org.bardframework.form.field.input.InputField;
import org.bardframework.form.model.filter.LocalDateTimeFilter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@Getter
@Setter
@ToString
public class DateTimeFilterField extends InputField<LocalDateTimeFilter> {
    private Long minLength;
    private Long maxLength;
    private ChronoUnit lengthUnit;
    private LocalDateTime minValue;
    private LocalDateTime maxValue;

    public DateTimeFilterField() {
    }

    public DateTimeFilterField(String name) {
        super(name);
    }

    @Override
    public FieldType getType() {
        return FilterFieldType.DATE_TIME_FILTER;
    }

    @Override
    public String toString(LocalDateTimeFilter value) {
        return String.join(SEPARATOR, null == value.getFrom() ? "" : value.getFrom().toString(), null == value.getTo() ? "" : value.getTo().toString());
    }
}