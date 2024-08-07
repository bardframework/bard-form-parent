package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.bardframework.commons.utils.DateTimeUtils;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;

import java.util.Locale;
import java.util.Map;

@Getter
@Setter
public class DateTimeFieldTemplate extends InputFieldTemplateAbstract<DateTimeField, Long> {

    private boolean minIsNow;
    private boolean maxIsNow;

    public DateTimeFieldTemplate(String name) {
        super(name);
    }

    @Override
    public boolean isValid(String flowToken, DateTimeField field, Long value, Map<String, String> flowData) {
        if (null == value) {
            if (Boolean.TRUE.equals(field.getRequired())) {
                log.debug("field [{}] is required, but it's value is empty", field.getName());
                return false;
            }
            return true;
        }
        if (null != field.getMinValue() && value < field.getMinValue()) {
            log.debug("field [{}] min value is [{}], but it's value is less than minimum", field.getName(), field.getMinValue());
            return false;
        }
        if (null != field.getMaxValue() && value > field.getMaxValue()) {
            log.debug("field [{}] max value is [{}], but it's value is greater than maximum", field.getName(), field.getMaxValue());
            return false;
        }
        return true;
    }

    @Override
    public void fill(FormTemplate formTemplate, DateTimeField field, Map<String, String> values, Locale locale) throws Exception {
        super.fill(formTemplate, field, values, locale);
        field.setMinValue(FormUtils.getFieldLongProperty(formTemplate, this, "minValue", locale, values, this.getDefaultValue().getMinValue()));
        field.setMaxValue(FormUtils.getFieldLongProperty(formTemplate, this, "maxValue", locale, values, this.getDefaultValue().getMaxValue()));
        if (null == field.getMinValue()) {
            field.setMinValue(this.getMinValue());
        }
        if (null == field.getMaxValue()) {
            field.setMaxValue(this.getMaxValue());
        }
    }

    @Override
    public Long toValue(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return Long.parseLong(value);
    }

    public Long getMinValue() {
        return minIsNow ? DateTimeUtils.getNowUtc() : null;
    }

    public Long getMaxValue() {
        return maxIsNow ? DateTimeUtils.getNowUtc() : null;
    }
}