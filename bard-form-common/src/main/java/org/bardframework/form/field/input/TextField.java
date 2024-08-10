package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bardframework.form.field.FieldType;

@Getter
@Setter
@ToString
public class TextField extends InputField<String> {
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

    public TextField(String name) {
        super(name);
    }

    @Override
    public FieldType getType() {
        return InputFieldType.TEXT;
    }

    @Override
    public String toString(String value) {
        return value;
    }
}