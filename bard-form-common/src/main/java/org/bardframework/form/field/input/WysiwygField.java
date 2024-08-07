package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bardframework.form.field.FieldType;

@Getter
@Setter
@ToString
public class WysiwygField extends InputField<String> {
    private Integer maxSize;

    public WysiwygField() {
    }

    protected WysiwygField(String name) {
        super(name);
    }

    @Override
    public FieldType getType() {
        return InputFieldType.WYSIWYG;
    }

    @Override
    public String toString(String value) {
        return value;
    }
}