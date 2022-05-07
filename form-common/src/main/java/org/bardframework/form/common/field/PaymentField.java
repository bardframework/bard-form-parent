package org.bardframework.form.common.field;

import org.bardframework.form.common.FieldType;
import org.bardframework.form.common.FieldTypeBase;
import org.bardframework.form.common.field.base.Field;

public class PaymentField extends Field {

    private Long amount;
    private String description;

    public PaymentField() {
    }

    @Override
    public FieldType<?> getType() {
        return FieldTypeBase.PAYMENT;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}