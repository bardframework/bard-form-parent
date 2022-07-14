package org.bardframework.form.field.view;

import org.bardframework.form.field.Field;
import org.bardframework.form.field.FieldType;

public class HtmlViewField extends Field {

    private String html;

    @Override
    public FieldType getType() {
        return ViewFieldType.HTML_VIEW;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }
}