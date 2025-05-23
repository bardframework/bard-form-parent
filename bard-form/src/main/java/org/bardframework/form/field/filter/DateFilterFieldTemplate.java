package org.bardframework.form.field.filter;

import lombok.Getter;
import lombok.Setter;
import org.bardframework.commons.utils.DateTimeUtils;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;
import org.bardframework.form.field.input.InputFieldTemplateAbstract;
import org.bardframework.form.model.filter.LongFilter;

import java.time.Period;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Getter
@Setter
public class DateFilterFieldTemplate extends InputFieldTemplateAbstract<DateFilterField, LongFilter> {
    private boolean minIsNow;
    private boolean maxIsNow;

    public DateFilterFieldTemplate(String name) {
        super(name);
    }

    @Override
    public void fill(FormTemplate formTemplate, DateFilterField field, Map<String, Object> values, Map<String, Object> args, Locale locale) throws Exception {
        super.fill(formTemplate, field, values, args, locale);
        field.setMinLength(FormUtils.getFieldLongProperty(formTemplate, this, "minLength", locale, args, this.getDefaultValue().getMinLength()));
        field.setMaxLength(FormUtils.getFieldLongProperty(formTemplate, this, "maxLength", locale, args, this.getDefaultValue().getMaxLength()));
        field.setMinValue(FormUtils.getFieldLongProperty(formTemplate, this, "minValue", locale, args, this.getDefaultValue().getMinValue()));
        field.setMaxValue(FormUtils.getFieldLongProperty(formTemplate, this, "maxValue", locale, args, this.getDefaultValue().getMaxValue()));
        if (null == field.getMinValue()) {
            field.setMinValue(this.getMinValue());
        }
        if (null == field.getMaxValue()) {
            field.setMaxValue(this.getMaxValue());
        }
    }

    @Override
    public LongFilter toValue(Object value) {
        if (null == value) {
            return null;
        }
        if (!(value instanceof List<?>)) {
            throw new IllegalStateException(value + " is not valid for: " + this.getClass().getName());
        }
        List<Long> parts = (List<Long>) value;
        if (parts.isEmpty()) {
            return null;
        }
        LongFilter filter = new LongFilter();
        filter.setFrom(parts.get(0));
        if (parts.size() > 1) {
            filter.setTo(parts.get(1));
        }
        return filter;
    }

    @Override
    public boolean isValid(String flowToken, DateFilterField field, LongFilter filter, Map<String, Object> flowData) {
        if (null == filter || (null == filter.getFrom() && null == filter.getTo())) {
            if (Boolean.TRUE.equals(field.getRequired())) {
                log.debug("filterField [{}] is required, but it's value is empty", field.getName());
                return false;
            }
            return true;
        }
        if (null != field.getMinValue()) {
            if (null != filter.getFrom() && filter.getFrom() < this.getMinValue()) {
                log.debug("field [{}] min value is [{}], but it's value is less than minimum", field.getName(), field.getMinValue());
                return false;
            }
            if (null != filter.getTo() && filter.getTo() < this.getMinValue()) {
                log.debug("field [{}] min value is [{}], but it's value is less than minimum", field.getName(), field.getMinValue());
                return false;
            }
        }
        if (null != field.getMaxValue()) {
            if (null != filter.getFrom() && filter.getFrom() > this.getMaxValue()) {
                log.debug("field [{}] max value is [{}], but it's value is greater than maximum", field.getName(), field.getMaxValue());
                return false;
            }
            if (null != filter.getTo() && filter.getTo() > this.getMaxValue()) {
                log.debug("field [{}] max value is [{}], but it's value is greater than maximum", field.getName(), field.getMaxValue());
                return false;
            }
        }
        long length = null == filter.getFrom() || null == filter.getTo() ? Long.MAX_VALUE : Period.between(DateTimeUtils.dateFromEpochMills(filter.getFrom()), DateTimeUtils.dateFromEpochMills(filter.getTo())).getDays() + 1;
        if (length < 0) {
            log.debug("values[{}] of range field[{}] is invalid, from is greater than to", filter, field.getName());
            /*
                from > to
             */
            return false;
        }
        if (null != field.getMinLength() && length < field.getMinLength()) {
            log.debug("field [{}] min length is [{}], but it's value length is smaller than minimum", field.getName(), field.getMaxLength());
            return false;
        }
        if (null != field.getMaxLength() && length > field.getMaxLength()) {
            log.debug("field [{}] max length is [{}], but it's value length is greater than maximum", field.getName(), field.getMaxLength());
            return false;
        }
        return true;
    }

    public Long getMinValue() {
        return minIsNow ? DateTimeUtils.getNowUtc() : null;
    }

    public Long getMaxValue() {
        return maxIsNow ? DateTimeUtils.getNowUtc() : null;
    }

}