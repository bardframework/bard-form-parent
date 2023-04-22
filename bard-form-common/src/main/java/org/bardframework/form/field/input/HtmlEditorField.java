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
public class HtmlEditorField extends InputField<String> {
    private Integer maxSize;

    public HtmlEditorField() {
    }

    protected HtmlEditorField(String name) {
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