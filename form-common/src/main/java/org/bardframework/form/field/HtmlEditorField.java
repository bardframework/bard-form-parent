package org.bardframework.form.field;

import org.bardframework.form.common.FormField;

public class HtmlEditorField extends FormField<String> {
    private Integer maxSize;

    public HtmlEditorField() {
    }

    protected HtmlEditorField(String name) {
        super(name);
    }

    @Override
    public FieldType getType() {
        return FieldType.HTML_EDITOR;
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