package org.bardframework.form.field;

import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;
import org.bardframework.form.field.base.FieldTemplate;
import org.bardframework.form.field.base.WithValueFieldTemplate;

import java.util.Locale;
import java.util.Map;

public class ReadOnlyFieldTemplate extends FieldTemplate<ReadonlyField> implements WithValueFieldTemplate<String> {
    private String tooltip;
    private String mask;
    private String placeholder;

    public ReadOnlyFieldTemplate(String name) {
        super(name);
    }

    @Override
    public void fill(FormTemplate formTemplate, ReadonlyField field, Map<String, String> args, Locale locale) throws Exception {
        super.fill(formTemplate, field, args, locale);
        field.setTooltip(FormUtils.getFieldStringProperty(formTemplate, this.getName(), "tooltip", locale, args, null));
        field.setMask(FormUtils.getFieldStringProperty(formTemplate, this.getName(), "mask", locale, args, null));
        field.setPlaceholder(FormUtils.getFieldStringProperty(formTemplate, this.getName(), "placeholder", locale, args, null));
    }

    @Override
    public String toValue(String stringValue) {
        return stringValue;
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
}
