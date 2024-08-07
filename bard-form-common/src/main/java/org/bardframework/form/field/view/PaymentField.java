package org.bardframework.form.field.view;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bardframework.form.field.Field;
import org.bardframework.form.field.FieldType;

@Getter
@Setter
@ToString
public class PaymentField extends Field {

    private Long amount;

    public PaymentField() {
    }

    @Override
    public FieldType getType() {
        return ViewFieldType.PAYMENT;
    }
}