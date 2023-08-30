package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.bardframework.form.FieldDescriptionShowType;
import org.bardframework.form.field.Field;

@Slf4j
@Getter
@Setter
@ToString
public abstract class InputField<T> extends Field {
    public static final String SEPARATOR = ",";
    protected FieldDescriptionShowType descriptionShowType;
    private String placeholder;
    private String errorMessage;
    private Boolean required;
    private Boolean disable;
    private T value;

    public InputField() {
    }

    protected InputField(String name) {
        super(name);
    }

    public abstract String toString(T value);
}
