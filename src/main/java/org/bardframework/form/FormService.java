package org.bardframework.form;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.bardframework.commons.utils.StringTemplateUtils;
import org.bardframework.form.exception.FormDataValidationException;
import org.bardframework.form.model.Form;
import org.bardframework.form.model.FormField;
import org.bardframework.form.template.DisabledTextFieldTemplate;
import org.bardframework.form.template.FormFieldTemplate;
import org.bardframework.form.template.FormTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public class FormService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FormService.class);

    private final MessageSource messageSource;

    public FormService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void validate(Form form, Map<String, String> values) throws FormDataValidationException {
        FormDataValidationException validationException = new FormDataValidationException();
        for (FormField field : form.getFields()) {
            if (Boolean.TRUE.equals(field.getDisable())) {
                continue;
            }
            if (!this.isValid(field, values.get(field.getName()))) {
                validationException.addFiledError(field.getName(), field.getErrorMessage());
            }
        }
        if (!MapUtils.isEmpty(validationException.getInvalidFields())) {
            throw validationException;
        }
    }

    public boolean isValid(String value, FormTemplate formTemplate, FormFieldTemplate fieldTemplate, Locale locale, Map<String, String> args) {
        FormField field = this.translate(formTemplate, fieldTemplate, locale, args);
        return this.isValid(field, value);
    }

    public boolean isValid(FormField field, String value) {
        if (StringUtils.isBlank(value)) {
            if (!Boolean.TRUE.equals(field.getDisable())) {
                /*
                    filed not required and value is empty
                 */
                return true;
            }
            LOGGER.debug("filed [{}] is required, but it's value is empty", field.getName());
            return false;
        }
        final String filedValue = value.trim();
        if (null != field.getMaxLength() && filedValue.length() > field.getMaxLength()) {
            LOGGER.debug("filed [{}] max length is [{}], but it's value length is [{}]", field.getName(), field.getMaxLength(), filedValue.length());
            return false;
        }
        if (null != field.getMinValue() && Long.parseLong(filedValue) < field.getMinValue()) {
            LOGGER.debug("filed [{}] min value is [{}], but it's value is less than minimum", field.getName(), field.getMinValue());
            return false;
        }
        if (null != field.getMaxValue() && Long.parseLong(filedValue) > field.getMaxValue()) {
            LOGGER.debug("filed [{}] max value is [{}], but it's value is greater than maximum", field.getName(), field.getMaxValue());
            return false;
        }
        if (StringUtils.isNoneBlank(field.getRegex()) && !Pattern.matches(field.getRegex(), filedValue)) {
            LOGGER.debug("filed [{}] regex is [{}], but it's value not match with it", field.getName(), field.getRegex());
            return false;
        }
        if (StringUtils.isNoneBlank(field.getPrefix()) && !filedValue.startsWith(field.getPrefix())) {
            LOGGER.debug("filed [{}] prefix is [{}], but it's value dose not start with it", field.getName(), field.getPrefix());
            return false;
        }
        if (StringUtils.isNoneBlank(field.getSuffix()) && !filedValue.endsWith(field.getSuffix())) {
            LOGGER.debug("filed [{}] suffix is [{}], but it's value dose not end with it", field.getName(), field.getSuffix());
            return false;
        }
        if (CollectionUtils.isNotEmpty(field.getOptions()) && field.getOptions().stream().noneMatch(baseData -> baseData.getId().equalsIgnoreCase(filedValue))) {
            LOGGER.debug("filed [{}] is select type, but it's value dose not equal with select options", field.getName());
            return false;
        }
        return true;
    }

    @Deprecated
    public <F extends Form> F translate(F form, FormTemplate formTemplate, Locale locale) {
        return this.translate(form, formTemplate, locale, Map.of());
    }

    public <F extends Form> F translate(F form, FormTemplate formTemplate, Locale locale, Map<String, String> args) {
        form.setId(formTemplate.getName());
        form.setTitle(this.getFormValue(formTemplate, "name", locale, args));
        form.setDescription(this.getFormValue(formTemplate, "description", locale, args));
        form.setConfirmMessage(this.getFormValue(formTemplate, "confirmMessage", locale, args));
        for (FormFieldTemplate fieldTemplate : formTemplate.getFields()) {
            form.addField(this.translate(formTemplate, fieldTemplate, locale, args));
        }
        return form;
    }

    public FormField translate(FormTemplate formTemplate, FormFieldTemplate fieldTemplate, Locale locale, Map<String, String> args) {
        FormField field = new FormField(fieldTemplate.getName(), fieldTemplate.getType());
        field.setValue(this.getFieldValue(formTemplate, fieldTemplate, "value", locale, args));
        field.setLabel(this.getFieldValue(formTemplate, fieldTemplate, "label", locale, args));
        field.setPlaceholder(this.getFieldValue(formTemplate, fieldTemplate, "placeholder", locale, args));
        field.setTooltip(this.getFieldValue(formTemplate, fieldTemplate, "tooltip", locale, args));
        field.setRegex(this.getFieldValue(formTemplate, fieldTemplate, "regex", locale, args));
        field.setRequired(this.getBooleanValue(formTemplate, fieldTemplate, "required", locale, args));
        field.setActive(this.getBooleanValue(formTemplate, fieldTemplate, "active", locale, args));
        if (fieldTemplate instanceof DisabledTextFieldTemplate) {
            field.setDisable(true);
        } else {
            field.setDisable(this.getBooleanValue(formTemplate, fieldTemplate, "disable", locale, args));
        }
        field.setMask(this.getFieldValue(formTemplate, fieldTemplate, "mask", locale, args));
        field.setPrefix(this.getFieldValue(formTemplate, fieldTemplate, "prefix", locale, args));
        field.setSuffix(this.getFieldValue(formTemplate, fieldTemplate, "suffix", locale, args));
        field.setMaxLength(this.getIntegerValue(formTemplate, fieldTemplate, "maxLength", locale, args));
        field.setShowInTable(this.getBooleanValue(formTemplate, fieldTemplate, "showInTable", locale, args));
        field.setErrorMessage(this.getFieldValue(formTemplate, fieldTemplate, "errorMessage", locale, args));
        field.setMinValue(this.getLongValue(formTemplate, fieldTemplate, "minValue", locale, args));
        field.setMaxValue(this.getLongValue(formTemplate, fieldTemplate, "maxValue", locale, args));
        if (null != fieldTemplate.getOptionsDataProvider()) {
            field.setOptions(fieldTemplate.getOptionsDataProvider().getOptions(messageSource, locale));
        }
        String newType = this.getFieldValue(formTemplate, fieldTemplate, "type", locale, args);
        if (StringUtils.isNotBlank(newType)) {
            field.setType(newType);
        }
        return field;
    }

    public String getFormValue(FormTemplate formTemplate, String property, Locale locale, Map<String, String> args) {
        String value = this.getString(formTemplate.getName() + "." + property, locale, args);
        if (null == value) {
            value = this.getString("form." + property, locale, args);
        }
        return StringTemplateUtils.fillTemplate(value, args);
    }

    /**
     * @return false if it can't find
     */
    public boolean getFormBooleanValue(FormTemplate formTemplate, String property, Locale locale, Map<String, String> args) {
        String value = this.getFormValue(formTemplate, property, locale, args);
        if (StringUtils.isBlank(value)) {
            return false;
        }
        try {
            return Boolean.parseBoolean(value);
        } catch (Exception e) {
            LOGGER.error("error reading [{}] of [{}] as boolean", property, formTemplate.getName(), e);
            return false;
        }
    }

    /**
     * @return 0 if can't read property value
     */
    public int getFormIntValue(FormTemplate formTemplate, String property, Locale locale, Map<String, String> args) {
        String value = this.getFormValue(formTemplate, property, locale, args);
        if (StringUtils.isBlank(value)) {
            return 0;
        }
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            LOGGER.error("error reading [{}] of [{}] as int", property, formTemplate.getName(), e);
            return 0;
        }
    }

    /**
     * @return null if can't read property value
     */
    public String getFieldValue(FormTemplate formTemplate, FormFieldTemplate fieldTemplate, String property, Locale locale, Map<String, String> args) {
        String value = this.getString(formTemplate.getName() + "." + fieldTemplate.getName() + "." + property, locale, args);
        if (null == value) {
            value = this.getString("field." + fieldTemplate.getName() + "." + property, locale, args);
        }
        return value;
    }

    /**
     * @return false if can't read property value
     */
    public Boolean getBooleanValue(FormTemplate formTemplate, FormFieldTemplate fieldTemplate, String property, Locale locale, Map<String, String> args) {
        String value = this.getFieldValue(formTemplate, fieldTemplate, property, locale, args);
        if (StringUtils.isBlank(value)) {
            return null;
        }
        try {
            return Boolean.parseBoolean(value);
        } catch (Exception e) {
            LOGGER.error("error reading [{}] of [{}.{}] as boolean", property, formTemplate.getName(), fieldTemplate.getName(), e);
            return null;
        }
    }

    /**
     * @return 0 if can't read property value
     */
    public int getIntValue(FormTemplate formTemplate, FormFieldTemplate fieldTemplate, String property, Locale locale, Map<String, String> args) {
        String value = this.getFieldValue(formTemplate, fieldTemplate, property, locale, args);
        if (StringUtils.isBlank(value)) {
            return 0;
        }
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            LOGGER.error("error reading [{}] of [{}.{}] as int", property, formTemplate.getName(), fieldTemplate.getName(), e);
            return 0;
        }
    }

    /**
     * @return null if can't read property value
     */
    public Integer getIntegerValue(FormTemplate formTemplate, FormFieldTemplate fieldTemplate, String property, Locale locale, Map<String, String> args) {
        String value = this.getFieldValue(formTemplate, fieldTemplate, property, locale, args);
        if (StringUtils.isBlank(value)) {
            return null;
        }
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            LOGGER.error("error reading [{}] of [{}.{}] as Integer", property, formTemplate.getName(), fieldTemplate.getName(), e);
            return null;
        }
    }

    /**
     * @return null if can't read property value
     */
    public Long getLongValue(FormTemplate formTemplate, FormFieldTemplate fieldTemplate, String property, Locale locale, Map<String, String> args) {
        String value = this.getFieldValue(formTemplate, fieldTemplate, property, locale, args);
        if (StringUtils.isBlank(value)) {
            return null;
        }
        try {
            return Long.parseLong(value);
        } catch (Exception e) {
            LOGGER.error("error reading [{}] of [{}.{}] as long", property, formTemplate.getName(), fieldTemplate.getName(), e);
            return null;
        }
    }

    public String getString(String key, Locale locale, Map<String, String> args) {
        String value = messageSource.getMessage(key, null, null, locale);
        return StringTemplateUtils.fillTemplate(value, args);
    }
}
