package org.bardframework.form.common.field;

import org.bardframework.form.common.FieldType;
import org.bardframework.form.common.FieldTypeBase;
import org.bardframework.form.common.field.base.FormField;

public class HtmlEditorField extends FormField<String> {
    private Integer maxSize;

    public HtmlEditorField() {
    }

    protected HtmlEditorField(String name) {
        super(name);
    }

    @Override
    public FieldType<?> getType() {
        return FieldTypeBase.HTML_EDITOR;
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