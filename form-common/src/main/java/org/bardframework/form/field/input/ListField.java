package org.bardframework.form.field.input;

import org.bardframework.form.field.InputField;

import java.util.List;

public class ListField extends InputField<List<String>> {
    private Integer minLength;
    private Integer maxLength;
    private Integer maxCount;

    public ListField() {
    }

    public ListField(String name) {
        super(name);
    }

    @Override
    public InputFieldType getType() {
        return InputFieldType.LIST;
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