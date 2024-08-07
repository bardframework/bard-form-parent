package org.bardframework.form.field.filter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bardframework.commons.utils.DateTimeUtils;
import org.bardframework.form.field.FieldType;
import org.bardframework.form.field.input.InputField;
import org.bardframework.form.model.filter.LocalDateFilter;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class DateFilterField extends InputField<LocalDateFilter> {
    private Long minLength;
    private Long maxLength;
    private LocalDate minValue;
    private LocalDate maxValue;

    public DateFilterField() {
    }

    public DateFilterField(String name) {
        super(name);
    }

    @Override
    public FieldType getType() {
        return FilterFieldType.DATE_FILTER;
    }

    @Override
    public String toString(LocalDateFilter value) {
        return String.join(SEPARATOR, null == value.getFrom() ? "" : value.getFrom().toString(), null == value.getTo() ? "" : value.getTo().toString());
    }
}