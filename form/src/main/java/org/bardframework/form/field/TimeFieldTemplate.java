package org.bardframework.form.field;

import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;
import org.bardframework.form.common.field.TimeField;
import org.bardframework.form.field.base.FormFieldTemplate;

import java.time.LocalTime;
import java.util.Locale;
import java.util.Map;

public class TimeFieldTemplate extends FormFieldTemplate<TimeField, LocalTime> {
    private boolean minIsNow;
    private boolean maxIsNow;

    protected TimeFieldTemplate(String name) {
        super(name);
    }

    @Override
    public boolean isValid(TimeField field, LocalTime value) {
        if (null == value && Boolean.TRUE.equals(field.getDisable())) {
            LOGGER.debug("filed [{}] is required, but it's value is empty", field.getName());
            return false;
        }
        if (null != field.getMinValue() && (null == value || value.isBefore(field.getMinValue()))) {
            LOGGER.debug("filed [{}] min value is [{}], but it's value is less than minimum", field.getName(), field.getMinValue());
            return false;
        }
        if (null != field.getMaxValue() && (null == value || value.isAfter(field.getMaxValue()))) {
            LOGGER.debug("filed [{}] max value is [{}], but it's value is greater than maximum", field.getName(), field.getMaxValue());
            return false;
        }
        return true;
    }

    @Override
    public TimeField getEmptyField() {
        return new TimeField();
    }

    @Override
    public void fill(FormTemplate formTemplate, TimeField field, Map<String, String> args, Locale locale) throws Exception {
        super.fill(formTemplate, field, args, locale);
        field.setMinValue(FormUtils.getLocalTimeValue(formTemplate, this.getName(), "minValue", locale, args));
        field.setMaxValue(FormUtils.getLocalTimeValue(formTemplate, this.getName(), "maxValue", locale, args));
        if (null == field.getMinValue()) {
            field.setMinValue(this.getMinValue());
        }
        if (null == field.getMaxValue()) {
            field.setMinValue(this.getMaxValue());
        }
    }

    @Override
    public LocalTime toValue(String value) {
        return LocalTime.parse(value);
    }

    public LocalTime getMinValue() {
        return minIsNow ? LocalTime.now() : null;
    }

    public LocalTime getMaxValue() {
        return maxIsNow ? LocalTime.now() : null;
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