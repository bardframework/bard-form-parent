package org.bardframework.form.field.input;

import org.bardframework.form.field.FieldType;

public class CaptchaField extends InputField<String> {

    private Integer length;
    private String refreshAction;

    public CaptchaField() {
    }

    protected CaptchaField(String name) {
        super(name);
    }

    @Override
    public FieldType getType() {
        return InputFieldType.CAPTCHA;
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