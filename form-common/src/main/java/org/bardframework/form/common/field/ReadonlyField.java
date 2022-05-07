package org.bardframework.form.common.field;

import org.bardframework.form.common.FieldType;
import org.bardframework.form.common.FieldTypeBase;
import org.bardframework.form.common.field.base.Field;
import org.bardframework.form.common.field.base.WithValueField;

public class ReadonlyField extends Field implements WithValueField<String> {

    private String tooltip;
    private String mask;
    private String placeholder;
    private String value;

    @Override
    public FieldType<?> getType() {
        return FieldTypeBase.READONLY;
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

    @Override
    public void setValue(String value) {
        this.value = value;
    }
}