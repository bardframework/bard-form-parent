package org.bardframework.form.field.input;

import org.bardframework.form.field.FieldType;

public class PasswordField extends InputField<String> {

    private String regex;
    private Integer minLength;
    private Integer maxLength;

    public PasswordField() {
    }

    protected PasswordField(String name) {
        super(name);
    }

    @Override
    public String toString(String value) {
        return value;
    }

    @Override
    public FieldType getType() {
        return InputFieldType.PASSWORD;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
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