package org.bardframework.form.field;

import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;
import org.bardframework.form.common.field.MessageField;
import org.bardframework.form.field.base.FieldTemplate;

import java.util.Locale;
import java.util.Map;

public class MessageFieldTemplate extends FieldTemplate<MessageField> {

    public MessageFieldTemplate(String name) {
        super(name);
    }

    @Override
    public void fill(FormTemplate formTemplate, MessageField field, Map<String, String> args, Locale locale) throws Exception {
        super.fill(formTemplate, field, args, locale);
        field.setMessage(FormUtils.getFieldValue(formTemplate, this.getName(), "message", locale, args));
    }

    @Override
    public MessageField getEmptyField() {
        return new MessageField();
    }
}
