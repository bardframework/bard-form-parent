package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;

import java.util.Locale;
import java.util.Map;

@Getter
@Setter
public class CardFieldTemplate extends InputFieldTemplateAbstract<CardField, String> {

    public CardFieldTemplate(String name) {
        super(name);
    }

    @Override
    public boolean isValid(String flowToken, CardField field, String value, Map<String, Object> flowData) {
        return null != value;
    }

    @Override
    public void fill(FormTemplate formTemplate, CardField field, Map<String, Object> values, Map<String, Object> args, Locale locale) throws Exception {
        super.fill(formTemplate, field, values, args, locale);
        field.setSubTitle(FormUtils.getFieldStringProperty(formTemplate, this, "subTitle", locale, args, this.getDefaultValue().getSubTitle()));
        field.setAvatarUrl(FormUtils.getFieldStringProperty(formTemplate, this, "avatarUrl", locale, args, this.getDefaultValue().getAvatarUrl()));
        field.setSelectLabel(FormUtils.getFieldStringProperty(formTemplate, this, "selectLabel", locale, args, this.getDefaultValue().getSelectLabel()));
    }
}