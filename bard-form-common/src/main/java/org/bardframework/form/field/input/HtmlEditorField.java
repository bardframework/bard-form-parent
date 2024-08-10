package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bardframework.form.field.FieldType;

@Getter
@Setter
@ToString
public class HtmlEditorField extends InputField<String> {
    private Integer maxSize;

    public HtmlEditorField() {
    }

    public HtmlEditorField(String name) {
        super(name);
    }

    @Override
    public FieldType getType() {
        return InputFieldType.HTML_EDITOR;
    }

    @Override
    public String toString(String value) {
        return value;
    }
}