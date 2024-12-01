package org.bardframework.form.field.filter;

import lombok.Getter;
import lombok.Setter;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;
import org.bardframework.form.field.input.InputFieldTemplateAbstract;
import org.bardframework.form.model.filter.StringFilter;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@Getter
@Setter
abstract class IpFilterFieldTemplateAbstract<F extends Ip4FilterField> extends InputFieldTemplateAbstract<F, StringFilter> {

    public IpFilterFieldTemplateAbstract(String name) {
        super(name);
    }

    @Override
    public boolean isValid(String flowToken, F field, StringFilter filter, Map<String, Object> flowData) {
        if (null == filter || (null == filter.getFrom() && null == filter.getTo())) {
            if (Boolean.TRUE.equals(field.getRequired())) {
                log.debug("filterField [{}] is required, but it's value is empty", field.getName());
                return false;
            }
            return true;
        }
        if (null != field.getMinValue()) {
            if (null != filter.getFrom() && filter.getFrom().compareTo(field.getMinValue()) < 0) {
                log.debug("field [{}] min value is [{}], but it's value is less than minimum", field.getName(), field.getMinValue());
                return false;
            }
            if (null != filter.getTo() && filter.getTo().compareTo(field.getMinValue()) < 0) {
                log.debug("field [{}] min value is [{}], but it's value is less than minimum", field.getName(), field.getMinValue());
                return false;
            }
        }
        if (null != field.getMaxValue()) {
            if (null != filter.getFrom() && filter.getFrom().compareTo(field.getMaxValue()) > 0) {
                log.debug("field [{}] max value is [{}], but it's value is greater than maximum", field.getName(), field.getMaxValue());
                return false;
            }
            if (null != filter.getTo() && filter.getTo().compareTo(field.getMaxValue()) > 0) {
                log.debug("field [{}] max value is [{}], but it's value is greater than maximum", field.getName(), field.getMaxValue());
                return false;
            }
        }
        if (null != filter.getFrom() && filter.getFrom().length() != this.getIpLength()) {
            log.debug("field [{}] value from [{}] length not is invalid, it's length must be {}.", field.getName(), filter.getFrom(), this.getIpLength());
            return false;
        }
        if (null != filter.getTo() && filter.getTo().length() != this.getIpLength()) {
            log.debug("field [{}] value to [{}] length not is invalid, it's length must be {}.", field.getName(), filter.getTo(), this.getIpLength());
            return false;
        }
        return true;
    }

    @Override
    public void fill(FormTemplate formTemplate, F field, Map<String, Object> values, Map<String, Object> args, Locale locale) throws Exception {
        super.fill(formTemplate, field, values, args, locale);
        field.setMinValue(FormUtils.getFieldStringProperty(formTemplate, this, "minValue", locale, args, this.getDefaultValue().getMinValue()));
        field.setMaxValue(FormUtils.getFieldStringProperty(formTemplate, this, "maxValue", locale, args, this.getDefaultValue().getMaxValue()));
    }

    @Override
    public StringFilter toValue(Object value) {
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
        StringFilter filter = new StringFilter();
        filter.setFrom(parts.get(0));
        if (parts.size() > 1) {
            filter.setTo(parts.get(1));
        }
        return filter;
    }

    protected abstract int getIpLength();
}