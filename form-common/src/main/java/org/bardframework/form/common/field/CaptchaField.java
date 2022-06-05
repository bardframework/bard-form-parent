package org.bardframework.form.common.field;

import com.fasterxml.jackson.annotation.JsonTypeName;
import org.bardframework.form.common.FieldType;
import org.bardframework.form.common.FieldTypeBase;
import org.bardframework.form.common.field.base.FormField;

@JsonTypeName("CAPTCHA")
public class CaptchaField extends FormField<String> {

    private Integer length;
    private String refreshAction;

    public CaptchaField() {
    }

    protected CaptchaField(String name) {
        super(name);
    }

    @Override
    public FieldType<?> getType() {
        return FieldTypeBase.CAPTCHA;
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