package org.bardframework.form.field.view;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bardframework.form.field.Field;
import org.bardframework.form.field.FieldType;

@Getter
@Setter
@ToString
public class HtmlViewField extends Field {

    private String html;

    @Override
    public FieldType getType() {
        return ViewFieldType.HTML_VIEW;
    }
}