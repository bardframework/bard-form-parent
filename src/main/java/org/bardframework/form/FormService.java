package org.bardframework.form;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.bardframework.form.exception.FormDataValidationException;
import org.bardframework.form.model.Form;
import org.bardframework.form.model.FormField;
import org.bardframework.form.model.Option;
import org.bardframework.form.model.OptionListPropertiesFile;
import org.bardframework.form.template.FormFieldTemplate;
import org.bardframework.form.template.FormTemplate;
import org.bardframework.form.template.OptionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import java.util.*;
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
            if (!this.isValid(field, values.get(field.getName()))) {
                validationException.addFiledError(field.getName(), field.getErrorMessage());
            }
        }
        if (!MapUtils.isEmpty(validationException.getInvalidFields())) {
            throw validationException;
        }
    }

    public boolean isValid(FormField field, String value) {
        if (StringUtils.isBlank(value)) {
            if (!field.isRequired()) {
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

    public <F extends Form> F translate(F form, FormTemplate formTemplate, Locale locale) {
        form.setId(formTemplate.getName());
        form.setTitle(this.getFormValue(formTemplate, "name", locale));
        form.setDescription(this.getFormValue(formTemplate, "description", locale));
        form.setEnable(this.getFormBooleanValue(formTemplate, "enable", locale));
        form.setOrder(this.getFormIntValue(formTemplate, "order", locale));
        for (FormFieldTemplate fieldTemplate : formTemplate.getFields()) {
            if (this.getBooleanValue(formTemplate, fieldTemplate, "enable", locale)) {
                form.addField(this.translate(formTemplate, fieldTemplate, locale));
            }
        }
        if (null != form.getFields()) {
            Collections.sort(form.getFields());
        }
        return form;
    }

    public FormField translate(FormTemplate formTemplate, FormFieldTemplate fieldTemplate, Locale locale) {
        FormField field = new FormField(fieldTemplate.getName(), fieldTemplate.getType());
        field.setValue(this.getFieldValue(formTemplate, fieldTemplate, "value", locale));
        field.setLabel(this.getFieldValue(formTemplate, fieldTemplate, "label", locale));
        field.setPlaceholder(this.getFieldValue(formTemplate, fieldTemplate, "placeholder", locale));
        field.setTooltip(this.getFieldValue(formTemplate, fieldTemplate, "tooltip", locale));
        field.setRegex(this.getFieldValue(formTemplate, fieldTemplate, "regex", locale));
        field.setRequired(this.getBooleanValue(formTemplate, fieldTemplate, "required", locale));
        field.setDisable(this.getBooleanValue(formTemplate, fieldTemplate, "disable", locale));
        field.setMask(this.getFieldValue(formTemplate, fieldTemplate, "mask", locale));
        field.setPrefix(this.getFieldValue(formTemplate, fieldTemplate, "prefix", locale));
        field.setSuffix(this.getFieldValue(formTemplate, fieldTemplate, "suffix", locale));
        field.setMaxLength(this.getIntegerValue(formTemplate, fieldTemplate, "maxLength", locale));
        field.setShowInTable(this.getBooleanValue(formTemplate, fieldTemplate, "showInTable", locale));
        field.setErrorMessage(this.getFieldValue(formTemplate, fieldTemplate, "errorMessage", locale));
        field.setOrder(this.getIntValue(formTemplate, fieldTemplate, "order", locale));
        field.setMinValue(this.getLongValue(formTemplate, fieldTemplate, "minValue", locale));
        field.setMaxValue(this.getLongValue(formTemplate, fieldTemplate, "maxValue", locale));
        if (null != fieldTemplate.getEnumOptionsClass()) {
            field.setOptions(this.getOptions(fieldTemplate.getEnumOptionsClass(), locale));
        }
        if (!CollectionUtils.isEmpty(fieldTemplate.getOptions())) {
            if (fieldTemplate.getOptions() instanceof OptionListPropertiesFile) {
                field.setOptions(((OptionListPropertiesFile) fieldTemplate.getOptions()).getOptions(locale));
            } else {
                field.setOptions(this.getOptions(fieldTemplate.getOptions(), locale));
            }
        }
        String newType = this.getFieldValue(formTemplate, fieldTemplate, "type", locale);
        if (StringUtils.isNotBlank(newType)) {
            field.setType(newType);
        }
        return field;
    }

    public List<Option> getOptions(List<OptionTemplate> fieldTemplates, Locale locale) {
        Collections.sort(fieldTemplates);
        List<Option> options = new ArrayList<>();
        for (OptionTemplate optionTemplate : fieldTemplates) {
            Option option = new Option();
            option.setId(optionTemplate.getId());
            option.setTitle(this.getString(optionTemplate.getName(), locale));
            options.add(option);
        }
        return options;
    }

    public List<Option> getOptions(Class<? extends Enum> clazz, Locale locale) {
        List<Option> options = new ArrayList<>();
        for (Enum anEnum : clazz.getEnumConstants()) {
            Option option = new Option();
            option.setId(anEnum.name());
            option.setTitle(this.getString(clazz.getSimpleName() + "." + anEnum.name(), locale));
            options.add(option);
        }
        return options;
    }

    public String getString(String key, Locale locale) {
        return messageSource.getMessage(key, null, null, locale);
    }

    public String getFormValue(FormTemplate formTemplate, String property, Locale locale) {
        String value = this.getString(formTemplate.getName() + "." + property, locale);
        if (null == value) {
            value = this.getString("form." + property, locale);
        }
        return value;
    }

    /**
     * @param formTemplate
     * @param property
     * @param locale
     * @return false if can't find
     */
    public boolean getFormBooleanValue(FormTemplate formTemplate, String property, Locale locale) {
        String value = this.getFormValue(formTemplate, property, locale);
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
     * @param formTemplate
     * @param property
     * @param locale
     * @return 0 if can't read property value
     */
    public int getFormIntValue(FormTemplate formTemplate, String property, Locale locale) {
        String value = this.getFormValue(formTemplate, property, locale);
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
     * @param formTemplate
     * @param fieldTemplate
     * @param property
     * @param locale
     * @return null if can't read property value
     */
    public String getFieldValue(FormTemplate formTemplate, FormFieldTemplate fieldTemplate, String property, Locale locale) {
        String value = this.getString(formTemplate.getName() + "." + fieldTemplate.getName() + "." + property, locale);
        if (null == value) {
            value = this.getString("field." + fieldTemplate.getName() + "." + property, locale);
        }
        return value;
    }

    /**
     * @param formTemplate
     * @param fieldTemplate
     * @param property
     * @param locale
     * @return false if can't read property value
     */
    public boolean getBooleanValue(FormTemplate formTemplate, FormFieldTemplate fieldTemplate, String property, Locale locale) {
        String value = this.getFieldValue(formTemplate, fieldTemplate, property, locale);
        if (StringUtils.isBlank(value)) {
            return false;
        }
        try {
            return Boolean.parseBoolean(value);
        } catch (Exception e) {
            LOGGER.error("error reading [{}] of [{}.{}] as boolean", property, formTemplate.getName(), fieldTemplate.getName(), e);
            return false;
        }
    }

    /**
     * @param formTemplate
     * @param fieldTemplate
     * @param property
     * @param locale
     * @return 0 if can't read property value
     */
    public int getIntValue(FormTemplate formTemplate, FormFieldTemplate fieldTemplate, String property, Locale locale) {
        String value = this.getFieldValue(formTemplate, fieldTemplate, property, locale);
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
     * @param formTemplate
     * @param fieldTemplate
     * @param property
     * @param locale
     * @return null if can't read property value
     */
    public Integer getIntegerValue(FormTemplate formTemplate, FormFieldTemplate fieldTemplate, String property, Locale locale) {
        String value = this.getFieldValue(formTemplate, fieldTemplate, property, locale);
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
     * @param formTemplate
     * @param fieldTemplate
     * @param property
     * @param locale
     * @return null if can't read property value
     */
    public Long getLongValue(FormTemplate formTemplate, FormFieldTemplate fieldTemplate, String property, Locale locale) {
        String value = this.getFieldValue(formTemplate, fieldTemplate, property, locale);
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
}
