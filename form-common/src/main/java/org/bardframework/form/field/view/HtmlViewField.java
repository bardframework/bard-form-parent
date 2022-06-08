package org.bardframework.form.field.view;

import org.bardframework.form.field.Field;

public class HtmlViewField extends Field {

    private String html;

    @Override
    public ViewFieldType getType() {
        return ViewFieldType.HTML_VIEW;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }
}