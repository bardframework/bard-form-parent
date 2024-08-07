package org.bardframework.form.field.view;

import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;
import org.bardframework.form.field.FieldTemplate;

import java.util.Locale;
import java.util.Map;

public class PaymentFieldTemplate extends FieldTemplate<PaymentField> {

    public PaymentFieldTemplate(String name) {
        super(name);
    }

    @Override
    public void fill(FormTemplate formTemplate, PaymentField field, Map<String, String> args, Locale locale) throws Exception {
        super.fill(formTemplate, field, args, locale);
        field.setAmount(FormUtils.getFieldLongProperty(formTemplate, this, "amount", locale, args, this.getDefaultValue().getAmount()));
    }
}