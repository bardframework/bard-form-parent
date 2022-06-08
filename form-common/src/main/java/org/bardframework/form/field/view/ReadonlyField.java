package org.bardframework.form.field.view;

import org.bardframework.form.field.Field;

public class ReadonlyField extends Field {

    private String tooltip;
    private String mask;
    private String placeholder;
    private String value;

    @Override
    public ViewFieldType getType() {
        return ViewFieldType.READONLY;
    }

    public String getTooltip() {
        return tooltip;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}