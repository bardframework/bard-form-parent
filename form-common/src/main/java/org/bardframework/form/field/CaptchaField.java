package org.bardframework.form.field;

import org.bardframework.form.common.FormField;

public class CaptchaField extends FormField<String> {

    private Integer length;
    private String refreshAction;

    public CaptchaField() {
    }

    protected CaptchaField(String name) {
        super(name);
    }

    @Override
    public FieldType getType() {
        return FieldType.CAPTCHA;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String getRefreshAction() {
        return refreshAction;
    }

    public void setRefreshAction(String refreshAction) {
        this.refreshAction = refreshAction;
    }

    @Override
    public String toString(String value) {
        return value;
    }
}