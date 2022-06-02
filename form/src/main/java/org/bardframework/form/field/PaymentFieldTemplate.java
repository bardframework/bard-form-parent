package org.bardframework.form.field;

import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;
import org.bardframework.form.common.field.PaymentField;
import org.bardframework.form.field.base.FieldTemplate;

import java.util.Locale;
import java.util.Map;

public class PaymentFieldTemplate extends FieldTemplate<PaymentField> {

    protected PaymentFieldTemplate(String name) {
        super(name);
    }

    @Override
    public void fill(FormTemplate formTemplate, PaymentField field, Map<String, String> args, Locale locale) throws Exception {
        super.fill(formTemplate, field, args, locale);
        field.setAmount(FormUtils.getLongValue(formTemplate, this.getName(), "amount", locale, args));
        field.setDescription(FormUtils.getFieldValue(formTemplate, this.getName(), "description", locale, args));
    }
}