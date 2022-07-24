package org.bardframework.form.field.input;

import org.bardframework.form.field.Field;

public abstract class InputField<T> extends Field {
    public final static String SEPARATOR = ",";

    private String name;
    private String placeholder;
    private String errorMessage;
    private Boolean required;
    private Boolean disable;
    private T value;

    public InputField() {
    }

    protected InputField(String name) {
        this.name = name;
    }

    public abstract String toString(T value);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Boolean getDisable() {
        return disable;
    }

    public void setDisable(Boolean disable) {
        this.disable = disable;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }
}
