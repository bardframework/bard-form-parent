package org.bardframework.form.common.field;

import org.bardframework.form.common.FieldType;
import org.bardframework.form.common.FieldTypeBase;
import org.bardframework.form.common.field.base.Field;

public class HtmlViewField extends Field {

    private String html;

    @Override
    public FieldType<?> getType() {
        return FieldTypeBase.HTML_VIEW;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }
}