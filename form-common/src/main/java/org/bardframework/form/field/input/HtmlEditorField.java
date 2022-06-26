package org.bardframework.form.field.input;

import org.bardframework.form.field.FieldType;

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

    public Integer getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(Integer maxSize) {
        this.maxSize = maxSize;
    }
}