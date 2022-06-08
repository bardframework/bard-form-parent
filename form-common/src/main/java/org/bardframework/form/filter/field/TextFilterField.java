package org.bardframework.form.filter.field;

import org.bardframework.form.common.FormField;
import org.bardframework.form.filter.FilterFieldType;
import org.bardframework.form.filter.StringFilter;

public class TextFilterField extends FormField<StringFilter> {
    private String regex;
    private String mask;
    /**
     * این مشخصه حداقل طول داده‌ی معتبر برای فیلد را مشخص می‌کند
     * در صورتی که مشخصه‌ی required معتبر باشد به این معنی است که مقدار این فیلد حداقل 1 است
     */
    private Integer minLength;
    private Integer maxLength;

    public TextFilterField() {
    }

    protected TextFilterField(String name) {
        super(name);
    }

    @Override
    public FilterFieldType getType() {
        return FilterFieldType.TEXT_FILTER;
    }

    @Override
    public String toString(StringFilter value) {
        //FIXME
        if (null == value) {
            return null;
        }
        return value.getContains();
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public Integer getMinLength() {
        return minLength;
    }

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }
}