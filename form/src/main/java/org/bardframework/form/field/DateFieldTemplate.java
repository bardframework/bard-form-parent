package org.bardframework.form.field;

import org.apache.commons.lang3.StringUtils;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;
import org.bardframework.form.common.field.DateField;
import org.bardframework.form.field.base.FormFieldTemplate;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Map;

public class DateFieldTemplate extends FormFieldTemplate<DateField, LocalDate> {
    private boolean minIsNow;
    private boolean maxIsNow;

    protected DateFieldTemplate(String name) {
        super(name);
    }

    @Override
    public boolean isValid(DateField field, LocalDate value) {
        if (null == value && Boolean.TRUE.equals(field.getDisable())) {
            LOGGER.debug("field [{}] is required, but it's value is empty", field.getName());
            return false;
        }
        if (null != field.getMinValue() && (null == value || value.isBefore(field.getMinValue()))) {
            LOGGER.debug("field [{}] min value is [{}], but it's value is less than minimum", field.getName(), field.getMinValue());
            return false;
        }
        if (null != field.getMaxValue() && (null == value || value.isAfter(field.getMaxValue()))) {
            LOGGER.debug("field [{}] max value is [{}], but it's value is greater than maximum", field.getName(), field.getMaxValue());
            return false;
        }
        return true;
    }

    @Override
    public void fill(FormTemplate formTemplate, DateField field, Map<String, String> args, Locale locale) throws Exception {
        super.fill(formTemplate, field, args, locale);
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
    public LocalDate toValue(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return LocalDate.parse(value);
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