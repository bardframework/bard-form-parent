package org.bardframework.form.field.input;

import org.apache.commons.lang3.StringUtils;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Map;

public class DateTimeFieldTemplate extends InputFieldTemplate<DateTimeField, LocalDateTime> {
    private boolean minIsNow;
    private boolean maxIsNow;

    protected DateTimeFieldTemplate(String name) {
        super(name);
    }

    @Override
    public boolean isValid(DateTimeField field, LocalDateTime value, Map<String, String> args) {
        if (null == value) {
            if (Boolean.TRUE.equals(field.getRequired())) {
                LOGGER.debug("field [{}] is required, but it's value is empty", field.getName());
                return false;
            }
            return true;
        }
        if (null != field.getMinValue() && value.isBefore(field.getMinValue())) {
            LOGGER.debug("field [{}] min value is [{}], but it's value is less than minimum", field.getName(), field.getMinValue());
            return false;
        }
        if (null != field.getMaxValue() && value.isAfter(field.getMaxValue())) {
            LOGGER.debug("field [{}] max value is [{}], but it's value is greater than maximum", field.getName(), field.getMaxValue());
            return false;
        }
        return true;
    }

    @Override
    public void fill(FormTemplate formTemplate, DateTimeField field, Map<String, String> args, Locale locale, HttpServletRequest httpRequest) throws Exception {
        super.fill(formTemplate, field, args, locale, httpRequest);
        field.setMinValue(FormUtils.getFieldLocalDateTimeProperty(formTemplate, this, "minValue", locale, args, this.getDefaultValues().getMinValue()));
        field.setMaxValue(FormUtils.getFieldLocalDateTimeProperty(formTemplate, this, "maxValue", locale, args, this.getDefaultValues().getMaxValue()));
        if (null == field.getMinValue()) {
            field.setMinValue(this.getMinValue());
        }
        if (null == field.getMaxValue()) {
            field.setMinValue(this.getMaxValue());
        }
    }

    @Override
    public LocalDateTime toValue(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return LocalDateTime.parse(value);
    }

    public LocalDateTime getMinValue() {
        return minIsNow ? LocalDateTime.now() : null;
    }

    public LocalDateTime getMaxValue() {
        return maxIsNow ? LocalDateTime.now() : null;
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