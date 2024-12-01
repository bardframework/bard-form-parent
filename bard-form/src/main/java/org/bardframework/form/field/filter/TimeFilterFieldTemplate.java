package org.bardframework.form.field.filter;

import lombok.Getter;
import lombok.Setter;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;
import org.bardframework.form.field.input.InputFieldTemplateAbstract;
import org.bardframework.form.model.filter.LocalTimeFilter;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Getter
@Setter
public class TimeFilterFieldTemplate extends InputFieldTemplateAbstract<TimeFilterField, LocalTimeFilter> {

    private boolean minIsNow;
    private boolean maxIsNow;
    private ChronoUnit lengthUnit = ChronoUnit.SECONDS;

    public TimeFilterFieldTemplate(String name) {
        super(name);
    }

    @Override
    public void fill(FormTemplate formTemplate, TimeFilterField field, Map<String, Object> values, Map<String, Object> args, Locale locale) throws Exception {
        super.fill(formTemplate, field, values, args, locale);
        field.setMinLength(FormUtils.getFieldIntegerProperty(formTemplate, this, "minLength", locale, args, this.getDefaultValue().getMinLength()));
        field.setMaxLength(FormUtils.getFieldIntegerProperty(formTemplate, this, "maxLength", locale, args, this.getDefaultValue().getMaxLength()));
        field.setMinValue(FormUtils.getFieldLocalTimeProperty(formTemplate, this, "minValue", locale, args, this.getDefaultValue().getMinValue()));
        field.setMaxValue(FormUtils.getFieldLocalTimeProperty(formTemplate, this, "maxValue", locale, args, this.getDefaultValue().getMaxValue()));
        field.setLengthUnit(field.getLengthUnit());
        if (null == field.getMinValue()) {
            field.setMinValue(this.getMinValue());
        }
        if (null == field.getMaxValue()) {
            field.setMaxValue(this.getMaxValue());
        }
    }

    @Override
    public LocalTimeFilter toValue(Object value) {
        if (null == value) {
            return null;
        }
        if (!(value instanceof List<?>)) {
            throw new IllegalStateException(value + " is not valid for: " + this.getClass().getName());
        }
        List<String> parts = (List<String>) value;
        if (parts.isEmpty()) {
            return null;
        }
        LocalTimeFilter filter = new LocalTimeFilter();
        filter.setFrom(LocalTime.parse(parts.get(0)));
        if (parts.size() > 1) {
            filter.setTo(LocalTime.parse(parts.get(1)));
        }
        return filter;
    }

    @Override
    public boolean isValid(String flowToken, TimeFilterField field, LocalTimeFilter filter, Map<String, Object> flowData) {
        if (null == filter || (null == filter.getFrom() && null == filter.getTo())) {
            if (Boolean.TRUE.equals(field.getRequired())) {
                log.debug("filterField [{}] is required, but it's value is empty", field.getName());
                return false;
            }
            return true;
        }
        if (null != field.getMinValue()) {
            if (null != filter.getFrom() && filter.getFrom().isBefore(this.getMinValue())) {
                log.debug("field [{}] min value is [{}], but it's value is less than minimum", field.getName(), field.getMinValue());
                return false;
            }
            if (null != filter.getTo() && filter.getTo().isBefore(this.getMinValue())) {
                log.debug("field [{}] min value is [{}], but it's value is less than minimum", field.getName(), field.getMinValue());
                return false;
            }
        }
        if (null != field.getMaxValue()) {
            if (null != filter.getFrom() && filter.getFrom().isAfter(this.getMaxValue())) {
                log.debug("field [{}] max value is [{}], but it's value is greater than maximum", field.getName(), field.getMaxValue());
                return false;
            }
            if (null != filter.getTo() && filter.getTo().isAfter(this.getMaxValue())) {
                log.debug("field [{}] max value is [{}], but it's value is greater than maximum", field.getName(), field.getMaxValue());
                return false;
            }
        }
        long length = null == filter.getFrom() || null == filter.getTo() ? Long.MAX_VALUE : Duration.between(filter.getFrom(), filter.getTo()).get(this.getLengthUnit()) + 1;
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

    public LocalTime getMinValue() {
        return minIsNow ? LocalTime.now() : null;
    }

    public LocalTime getMaxValue() {
        return maxIsNow ? LocalTime.now() : null;
    }
}
