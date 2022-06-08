package org.bardframework.form.field;

import org.bardframework.form.common.Field;

public class HtmlViewField extends Field {

    private String html;

    @Override
    public FieldType getType() {
        return FieldType.HTML_VIEW;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }
}