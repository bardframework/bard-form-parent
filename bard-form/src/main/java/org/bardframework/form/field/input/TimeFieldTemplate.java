package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;

import java.time.LocalTime;
import java.util.Locale;
import java.util.Map;

@Getter
@Setter
public class TimeFieldTemplate extends InputFieldTemplateAbstract<TimeField, LocalTime> {
    private boolean minIsNow;
    private boolean maxIsNow;

    public TimeFieldTemplate(String name) {
        super(name);
    }

    @Override
    public boolean isValid(String flowToken, TimeField field, LocalTime value, Map<String, Object> flowData) {
        if (null == value) {
            if (Boolean.TRUE.equals(field.getRequired())) {
                log.debug("field [{}] is required, but it's value is empty", field.getName());
                return false;
            }
            return true;
        }
        if (null != field.getMinValue() && value.isBefore(field.getMinValue())) {
            log.debug("field [{}] min value is [{}], but it's value is less than minimum", field.getName(), field.getMinValue());
            return false;
        }
        if (null != field.getMaxValue() && value.isAfter(field.getMaxValue())) {
            log.debug("field [{}] max value is [{}], but it's value is greater than maximum", field.getName(), field.getMaxValue());
            return false;
        }
        return true;
    }

    @Override
    public void fill(FormTemplate formTemplate, TimeField field, Map<String, Object> values, Map<String, Object> args, Locale locale) throws Exception {
        super.fill(formTemplate, field, values, args, locale);
        field.setMinValue(FormUtils.getFieldLocalTimeProperty(formTemplate, this, "minValue", locale, args, this.getDefaultValue().getMinValue()));
        field.setMaxValue(FormUtils.getFieldLocalTimeProperty(formTemplate, this, "maxValue", locale, args, this.getDefaultValue().getMaxValue()));
        if (null == field.getMinValue()) {
            field.setMinValue(this.getMinValue());
        }
        if (null == field.getMaxValue()) {
            field.setMaxValue(this.getMaxValue());
        }
    }

    @Override
    public LocalTime toValue(Object value) {
        if (null == value) {
            return null;
        }
        if (!(value instanceof String)) {
            throw new IllegalStateException(value + " is not valid for: " + this.getClass().getName());
        }
        if (StringUtils.isBlank(value.toString())) {
            return null;
        }
        return LocalTime.parse(((String) value).trim());
    }

    protected LocalTime getMinValue() {
        return minIsNow ? LocalTime.now() : null;
    }

    protected LocalTime getMaxValue() {
        return maxIsNow ? LocalTime.now() : null;
    }
}