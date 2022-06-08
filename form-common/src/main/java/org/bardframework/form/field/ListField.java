package org.bardframework.form.field;

import org.bardframework.form.common.FormField;

import java.util.List;

public class ListField extends FormField<List<String>> {
    private Integer minLength;
    private Integer maxLength;
    private Integer maxCount;

    public ListField() {
    }

    public ListField(String name) {
        super(name);
    }

    @Override
    public FieldType getType() {
        return FieldType.LIST;
    }

    @Override
    public String toString(List<String> values) {
        return null == values ? null : String.join(SEPARATOR, values);
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

    public Integer getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(Integer maxCount) {
        this.maxCount = maxCount;
    }
}