package org.bardframework.flow.form.field.input.phonenumber;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;
import org.bardframework.form.exception.FormDataValidationException;
import org.bardframework.form.field.input.CountrySelectField;
import org.bardframework.form.field.input.CountrySelectFieldTemplate;
import org.bardframework.form.field.input.InputFieldTemplate;

import java.util.Locale;
import java.util.Map;

@Slf4j
public class PhoneNumberFieldTemplate extends InputFieldTemplate<PhoneNumberField, String> {

    private final PhoneNumberParser phoneNumberParser;
    private final CountrySelectFieldTemplate countrySelectFieldTemplate;

    protected PhoneNumberFieldTemplate(String name, PhoneNumberParser phoneNumberParser, CountrySelectFieldTemplate countrySelectFieldTemplate) {
        super(name);
        this.phoneNumberParser = phoneNumberParser;
        this.countrySelectFieldTemplate = countrySelectFieldTemplate;
    }

    @Override
    public void validate(String flowToken, FormTemplate formTemplate, Map<String, String> flowData, Map<String, String> formData, Locale locale, FormDataValidationException ex) throws Exception {
        PhoneNumberField formField = this.toField(formTemplate, formData, locale);
        if (Boolean.TRUE.equals(formField.getDisable())) {
            return;
        }
        String fieldName = this.getName();
        String stringValue = formData.get(fieldName);
        try {
            String value = this.toValue(stringValue);
            if (!this.isValid(flowToken, formField, value, formData)) {
                ex.addFieldError(fieldName, formField.getErrorMessage());
            }
            String phoneNumberString = formData.get(fieldName);
            if (null == phoneNumberString) {
                ex.addFieldError(fieldName, formField.getErrorMessage());
            }
            PhoneNumber phoneNumber = phoneNumberParser.parse(phoneNumberString);
            formData.put(countrySelectFieldTemplate.getName(), phoneNumber.getCountryAlphaCode());
            countrySelectFieldTemplate.validate(flowToken, formTemplate, flowData, formData, locale, ex);
        } catch (Exception e) {
            log.error("Unknown error: {} in validation field {}", e.getMessage(), fieldName);
            ex.addFieldError(fieldName, formField.getErrorMessage());
        }
    }

    @Override
    public boolean isValid(String flowToken, PhoneNumberField field, String value, Map<String, String> flowData) {
        if (null == value) {
            if (Boolean.TRUE.equals(field.getRequired())) {
                log.debug("field [{}] is required, but it's value is empty", field.getName());
                return false;
            }
            return true;
        }
        PhoneNumber phoneNumber = phoneNumberParser.parse(value);
        if (null != field.getMaxLength() && phoneNumber.getFullNumber().length() > field.getMaxLength()) {
            log.debug("field [{}] max length is [{}], but it's value[{}] length is greater than maximum", field.getName(), field.getMaxLength(), phoneNumber);
            return false;
        }
        return true;
    }

    @Override
    public void fill(FormTemplate formTemplate, PhoneNumberField field, Map<String, String> args, Locale locale) throws Exception {
        super.fill(formTemplate, field, args, locale);
        field.setMaxLength(FormUtils.getFieldIntegerProperty(formTemplate, this.getName(), "maxLength", locale, args, this.getDefaultValues().getMaxLength()));
        CountrySelectField countrySelectField = new CountrySelectField();
        countrySelectFieldTemplate.fill(formTemplate, countrySelectField, args, locale);
        field.setCountrySelectField(countrySelectField);
    }

    @Override
    public String toValue(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return phoneNumberParser.parse(value).getFullNumber();
    }
}
