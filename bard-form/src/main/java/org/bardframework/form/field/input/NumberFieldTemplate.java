package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;

import java.util.Locale;
import java.util.Map;

@Getter
@Setter
public class NumberFieldTemplate extends InputFieldTemplateAbstract<NumberField, Long> {

    public NumberFieldTemplate(String name) {
        super(name);
    }

    @Override
    public boolean isValid(String flowToken, NumberField field, Long value, Map<String, Object> flowData) {
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
    public void fill(FormTemplate formTemplate, NumberField field, Map<String, Object> values, Map<String, Object> args, Locale locale) throws Exception {
        super.fill(formTemplate, field, values, args, locale);
        field.setMinValue(this.getMinValue(formTemplate, args, locale));
        field.setMaxValue(this.getMaxValue(formTemplate, args, locale));
        field.setMask(FormUtils.getFieldStringProperty(formTemplate, this, "mask", locale, args, this.getDefaultValue().getMask()));
    }

    @Override
    public Long toValue(Object value) {
        if (null == value) {
            return null;
        }
        return Long.valueOf(value.toString());
    }

    private Long getMinValue(FormTemplate formTemplate, Map<String, Object> values, Locale locale) {
        Long minValue = FormUtils.getFieldLongProperty(formTemplate, this, "minValue", locale, values, this.getDefaultValue().getMinValue());
        if (null == minValue) {
            return Long.MIN_VALUE;
        }
        return minValue;
    }

    private Long getMaxValue(FormTemplate formTemplate, Map<String, Object> values, Locale locale) {
        Long maxValue = FormUtils.getFieldLongProperty(formTemplate, this, "maxValue", locale, values, this.getDefaultValue().getMaxValue());
        if (null == maxValue) {
            return Long.MAX_VALUE;
        }
        return maxValue;
    }

    public String toString(Long value) {
        return null == value ? null : value.toString();
    }
}