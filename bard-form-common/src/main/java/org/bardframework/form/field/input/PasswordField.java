package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.bardframework.form.field.FieldType;

@Slf4j
@Getter
@Setter
@ToString
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
}