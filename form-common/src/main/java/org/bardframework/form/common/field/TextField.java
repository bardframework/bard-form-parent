package org.bardframework.form.common.field;

import com.fasterxml.jackson.annotation.JsonTypeName;
import org.bardframework.form.common.FieldType;
import org.bardframework.form.common.FieldTypeBase;
import org.bardframework.form.common.field.base.FormField;

@JsonTypeName("TEXT")
public class TextField extends FormField<String> {
    private String regex;
    private String mask;
    /**
     * این مشخصه حداقل طول داده‌ی معتبر برای فیلد را مشخص می‌کند
     * در صورتی که مشخصه‌ی required معتبر باشد به این معنی است که مقدار این فیلد حداقل 1 است
     */
    private Integer minLength;
    private Integer maxLength;

    public TextField() {
    }

    protected TextField(String name) {
        super(name);
    }

    @Override
    public FieldType<?> getType() {
        return FieldTypeBase.TEXT;
    }

    @Override
    public String toString(String value) {
        return value;
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