package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bardframework.form.field.FieldType;

@Getter
@Setter
@ToString
public class PasswordField extends InputField<String> {

    private String regex;
    private Integer minLength;
    private Integer maxLength;

    public PasswordField() {
    }

    public PasswordField(String name) {
        super(name);
    }

    @Override
    public FieldType getType() {
        return InputFieldType.PASSWORD;
    }
}