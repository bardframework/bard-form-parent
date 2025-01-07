package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.common.CountryPhoneNumberInfo;
import org.bardframework.form.common.PhoneNumber;
import org.bardframework.form.common.PhoneNumberParser;
import org.bardframework.form.exception.FormDataValidationException;

import java.util.Locale;
import java.util.Map;

@Getter
@Setter
public class PhoneNumberFieldTemplate extends InputFieldTemplateAbstract<PhoneNumberField, String> {

    private final PhoneNumberParser phoneNumberParser;
    private final CountrySelectFieldTemplate countrySelectFieldTemplate;

    public PhoneNumberFieldTemplate(String name, PhoneNumberParser phoneNumberParser, CountrySelectFieldTemplate countrySelectFieldTemplate) {
        super(name);
        this.phoneNumberParser = phoneNumberParser;
        this.countrySelectFieldTemplate = countrySelectFieldTemplate;
    }

    @Override
    public void validate(String flowToken, FormTemplate formTemplate, Map<String, Object> flowData, Map<String, Object> formData, Locale locale, FormDataValidationException ex) throws Exception {
        PhoneNumberField formField = this.toField(formTemplate, flowData, formData, locale);
        if (Boolean.TRUE.equals(formField.getDisable())) {
            return;
        }
        String fieldName = this.getName();
        Object value = formData.get(fieldName);
        try {
            String cleanValue = this.toValue(value);
            if (!this.isValid(flowToken, formField, cleanValue, formData)) {
                ex.addFieldError(fieldName, formField.getErrorMessage());
                return;
            }
            Object phoneNumberString = formData.get(fieldName);
            if (null == phoneNumberString) {
                ex.addFieldError(fieldName, formField.getErrorMessage());
                return;
            }
            PhoneNumber phoneNumber = phoneNumberParser.parse(phoneNumberString.toString());
            if (null == phoneNumber) {
                ex.addFieldError(fieldName, formField.getErrorMessage());
                return;
            }
            formData.put(countrySelectFieldTemplate.getName(), phoneNumber.getCountryAlphaCode());
            countrySelectFieldTemplate.validate(flowToken, formTemplate, flowData, formData, locale, ex);
        } catch (Exception e) {
            log.error("Unknown error: {} in validation field {}", e.getMessage(), fieldName);
            ex.addFieldError(fieldName, formField.getErrorMessage());
        }
    }

    @Override
    public boolean isValid(String flowToken, PhoneNumberField field, String value, Map<String, Object> flowData) {
        if (null == value) {
            if (Boolean.TRUE.equals(field.getRequired())) {
                log.debug("field [{}] is required, but it's value is empty", field.getName());
                return false;
            }
            return true;
        }
        PhoneNumber phoneNumber = phoneNumberParser.parse(value);
        if (null == phoneNumber || (null != field.getMaxLength() && phoneNumber.getFullNumber().length() > field.getMaxLength())) {
            log.debug("field [{}] max length is [{}], but it's value[{}] length is greater than maximum", field.getName(), field.getMaxLength(), phoneNumber);
            return false;
        }
        return true;
    }

    @Override
    public void fill(FormTemplate formTemplate, PhoneNumberField field, Map<String, Object> values, Map<String, Object> args, Locale locale) throws Exception {
        super.fill(formTemplate, field, values, args, locale);
        field.setMaxLength(CountryPhoneNumberInfo.DE.getMaxLength() + String.valueOf(CountryPhoneNumberInfo.DE.getCountryCode()).length());
        CountrySelectField countrySelectField = new CountrySelectField();
        countrySelectFieldTemplate.fill(formTemplate, countrySelectField, values, args, locale);
        field.setCountrySelectField(countrySelectField);
    }

    @Override
    public String toValue(Object value) {
        if (null == value) {
            return null;
        }
        if (!(value instanceof String)) {
            throw new IllegalStateException(value + " is not valid for: " + this.getClass().getName());
        }
        if (StringUtils.isBlank(value.toString())) {
            return null;
        }
        return phoneNumberParser.parse(value.toString()).getFullNumber();
    }
}
