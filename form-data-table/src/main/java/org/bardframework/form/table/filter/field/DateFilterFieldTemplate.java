package org.bardframework.form.table.filter.field;

import org.apache.commons.lang3.StringUtils;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;
import org.bardframework.form.common.FormField;
import org.bardframework.form.field.base.FormFieldTemplate;
import org.bardframework.form.filter.LocalDateFilter;
import org.bardframework.form.filter.field.DateFilterField;

import java.time.LocalDate;
import java.time.Period;
import java.util.Locale;
import java.util.Map;

public class DateFilterFieldTemplate extends FormFieldTemplate<DateFilterField, LocalDateFilter> {
    private boolean minIsNow;
    private boolean maxIsNow;

    protected DateFilterFieldTemplate(String name) {
        super(name);
    }

    @Override
    public void fill(FormTemplate formTemplate, DateFilterField field, Map<String, String> args, Locale locale) throws Exception {
        super.fill(formTemplate, field, args, locale);
        field.setMinLength(FormUtils.getFieldLongProperty(formTemplate, this.getName(), "minLength", locale, args, null));
        field.setMaxLength(FormUtils.getFieldLongProperty(formTemplate, this.getName(), "maxLength", locale, args, null));
        field.setMinValue(FormUtils.getFieldLocalDateProperty(formTemplate, this.getName(), "minValue", locale, args, null));
        field.setMaxValue(FormUtils.getFieldLocalDateProperty(formTemplate, this.getName(), "maxValue", locale, args, null));
        if (null == field.getMinValue()) {
            field.setMinValue(this.getMinValue());
        }
        if (null == field.getMaxValue()) {
            field.setMinValue(this.getMaxValue());
        }
    }

    @Override
    public LocalDateFilter toValue(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        String[] parts = value.split(FormField.SEPARATOR);
        if (parts.length != 2) {
            throw new IllegalStateException(value + " is not valid for range value");
        }
        LocalDateFilter filter = new LocalDateFilter();
        if (!parts[0].isBlank()) {
            filter.setFrom(LocalDate.parse(parts[0]));
        }
        if (!parts[1].isBlank()) {
            filter.setTo(LocalDate.parse(parts[1]));
        }
        return filter;
    }

    @Override
    public boolean isValid(DateFilterField field, LocalDateFilter filter) {
        if (null == filter || (null == filter.getFrom() && null == filter.getTo())) {
            if (Boolean.TRUE.equals(field.getRequired())) {
                LOGGER.debug("filterField [{}] is required, but it's value is empty", field.getName());
                return false;
            }
            return true;
        }
        if (null != field.getMinValue()) {
            if (null != filter.getFrom() && filter.getFrom().isBefore(this.getMinValue())) {
                LOGGER.debug("field [{}] min value is [{}], but it's value is less than minimum", field.getName(), field.getMinValue());
                return false;
            }
            if (null != filter.getTo() && filter.getTo().isBefore(this.getMinValue())) {
                LOGGER.debug("field [{}] min value is [{}], but it's value is less than minimum", field.getName(), field.getMinValue());
                return false;
            }
        }
        if (null != field.getMaxValue()) {
            if (null != filter.getFrom() && filter.getFrom().isAfter(this.getMaxValue())) {
                LOGGER.debug("field [{}] max value is [{}], but it's value is greater than maximum", field.getName(), field.getMaxValue());
                return false;
            }
            if (null != filter.getTo() && filter.getTo().isAfter(this.getMaxValue())) {
                LOGGER.debug("field [{}] max value is [{}], but it's value is greater than maximum", field.getName(), field.getMaxValue());
                return false;
            }
        }
        long length = null == filter.getFrom() || null == filter.getTo() ? Long.MAX_VALUE : Period.between(filter.getFrom(), filter.getTo()).getDays() + 1;
        if (length < 0) {
            LOGGER.debug("values[{}] of range field[{}] is invalid, from is greater than to", filter, field.getName());
            /*
                from > to
             */
            return false;
        }
        if (null != field.getMinLength() && length < field.getMinLength()) {
            LOGGER.debug("field [{}] min length is [{}], but it's value length is smaller than minimum", field.getName(), field.getMaxLength());
            return false;
        }
        if (null != field.getMaxLength() && length > field.getMaxLength()) {
            LOGGER.debug("field [{}] max length is [{}], but it's value length is greater than maximum", field.getName(), field.getMaxLength());
            return false;
        }
        return true;
    }

    public LocalDate getMinValue() {
        return minIsNow ? LocalDate.now() : null;
    }

    public LocalDate getMaxValue() {
        return maxIsNow ? LocalDate.now() : null;
    }

    public boolean isMinIsNow() {
        return minIsNow;
    }

    public void setMinIsNow(boolean minIsNow) {
        this.minIsNow = minIsNow;
    }

    public boolean isMaxIsNow() {
        return maxIsNow;
    }

    public void setMaxIsNow(boolean maxIsNow) {
        this.maxIsNow = maxIsNow;
    }
}