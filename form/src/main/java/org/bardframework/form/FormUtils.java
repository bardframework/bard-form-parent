package org.bardframework.form;

import org.apache.commons.lang3.StringUtils;
import org.bardframework.commons.utils.StringTemplateUtils;
import org.bardframework.form.common.Form;
import org.bardframework.form.common.field.base.Field;
import org.bardframework.form.common.field.base.WithValueField;
import org.bardframework.form.field.base.FieldTemplate;
import org.bardframework.form.field.base.WithValueFieldTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class FormUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(FormUtils.class);

    private FormUtils() {
        /*
            prevent instantiation
         */
    }

    public static Form toForm(FormTemplate formTemplate, Locale locale, Map<String, String> args, Map<String, String> values) throws Exception {
        if (null == formTemplate) {
            return null;
        }
        return FormUtils.toForm(new Form(), formTemplate, locale, args, values);
    }

    public static Form toForm(FormTemplate formTemplate, Locale locale, Map<String, String> args) throws Exception {
        if (null == formTemplate) {
            return null;
        }
        return FormUtils.toForm(new Form(), formTemplate, locale, args, Map.of());
    }

    public static <F extends Form> F toForm(F form, FormTemplate formTemplate, Locale locale, Map<String, String> args) throws Exception {
        return FormUtils.toForm(form, formTemplate, locale, args, Map.of());
    }

    public static <F extends Form, T> F toForm(F form, FormTemplate formTemplate, Locale locale, Map<String, String> args, Map<String, String> values) throws Exception {
        form.setName(formTemplate.getName());
        form.setTitle(FormUtils.getFormStringProperty(formTemplate, "name", locale, args, formTemplate.getTitle()));
        form.setHint(FormUtils.getFormStringProperty(formTemplate, "description", locale, args, formTemplate.getHint()));
        form.setConfirmMessage(FormUtils.getFormStringProperty(formTemplate, "confirmMessage", locale, args, formTemplate.getConfirmMessage()));
        for (FieldTemplate<?> fieldTemplate : formTemplate.getFieldTemplates()) {
            Field field = fieldTemplate.toField(formTemplate, args, locale);
            String valueString = values.get(fieldTemplate.getName());
            if (field instanceof WithValueField && fieldTemplate instanceof WithValueFieldTemplate) {
                WithValueFieldTemplate<T> withValueFieldTemplate = (WithValueFieldTemplate<T>) fieldTemplate;
                WithValueField<T> withValueField = (WithValueField<T>) field;
                withValueField.setValue(withValueFieldTemplate.toValue(valueString));
            }
            form.addField(field);
        }
        return form;
    }

    /**
     * @return false if we can't find
     */
    public static Boolean getFormBooleanProperty(FormTemplate formTemplate, String property, Locale locale, Map<String, String> args, Boolean defaultValue) {
        String value = FormUtils.getFormStringProperty(formTemplate, property, locale, args, null);
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        try {
            return Boolean.parseBoolean(value);
        } catch (Exception e) {
            LOGGER.error("error reading [{}] of [{}] as boolean", property, formTemplate.getName(), e);
            return null;
        }
    }


    public static String getFormStringProperty(FormTemplate formTemplate, String property, Locale locale, Map<String, String> args, String defaultValue) {
        return FormUtils.getFormStringProperty(formTemplate.getName(), property, locale, args, defaultValue, formTemplate.getMessageSource());
    }

    public static String getFormStringProperty(String formName, String property, Locale locale, Map<String, String> args, String defaultValue, MessageSource messageSource) {
        String value = FormUtils.getString(formName + "." + property, locale, args, null, messageSource);
        if (null == value) {
            value = FormUtils.getString("form." + property, locale, args, defaultValue, messageSource);
        }
        return StringTemplateUtils.fillTemplate(value, args);
    }

    /**
     * @return null if we can't read property value
     */
    public static Boolean getFieldBooleanProperty(FormTemplate FormTemplate, String fieldName, String property, Locale locale, Map<String, String> args, Boolean defaultValue) {
        String value = FormUtils.getFieldStringProperty(FormTemplate, fieldName, property, locale, args, null);
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        try {
            return Boolean.parseBoolean(value);
        } catch (Exception e) {
            LOGGER.error("error reading [{}] of [{}.{}] as boolean", property, FormTemplate, fieldName, e);
            return null;
        }
    }

    /**
     * @return false if we can't read property value
     */
    public static List<String> getFieldListProperty(FormTemplate formTemplate, String fieldName, String property, Locale locale, Map<String, String> args, List<String> defaultValue) {
        String value = FormUtils.getFieldStringProperty(formTemplate, fieldName, property, locale, args, null);
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        return Arrays.stream(value.split(",")).map(String::trim).collect(Collectors.toList());
    }

    /**
     * @return null if we can't read property value
     */
    public static Integer getFieldIntegerProperty(FormTemplate formTemplate, String fieldName, String property, Locale locale, Map<String, String> args, Integer defaultValue) {
        String value = FormUtils.getFieldStringProperty(formTemplate, fieldName, property, locale, args, null);
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            LOGGER.error("error reading [{}] of [{}.{}] as Integer", property, formTemplate, fieldName, e);
            return null;
        }
    }

    /**
     * @return null if we can't read property value
     */
    public static Double getFieldDoubleProperty(FormTemplate formTemplate, String fieldName, String property, Locale locale, Map<String, String> args, Double defaultValue) {
        String value = FormUtils.getFieldStringProperty(formTemplate, fieldName, property, locale, args, null);
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            LOGGER.error("error reading [{}] of [{}.{}] as Double", property, formTemplate, fieldName, e);
            return null;
        }
    }

    /**
     * @return null if we can't read property value
     */
    public static Long getFieldLongProperty(FormTemplate formTemplate, String fieldName, String property, Locale locale, Map<String, String> args, Long defaultValue) {
        String value = FormUtils.getFieldStringProperty(formTemplate, fieldName, property, locale, args, null);
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        try {
            return Long.parseLong(value);
        } catch (Exception e) {
            LOGGER.error("error reading [{}] of [{}.{}] as Long", property, formTemplate, fieldName, e);
            return null;
        }
    }

    /**
     * @return null if we can't read property value
     */
    public static LocalDate getFieldLocalDateProperty(FormTemplate formTemplate, String fieldName, String property, Locale locale, Map<String, String> args, LocalDate defaultValue) {
        String value = FormUtils.getFieldStringProperty(formTemplate, fieldName, property, locale, args, null);
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        try {
            return LocalDate.parse(value);
        } catch (Exception e) {
            LOGGER.error("error reading [{}] of [{}.{}] as LocalDate", property, formTemplate, fieldName, e);
            return null;
        }
    }

    public static LocalDateTime getFieldLocalDateTimeProperty(FormTemplate formTemplate, String fieldName, String property, Locale locale, Map<String, String> args, LocalDateTime defaultValue) {
        String value = FormUtils.getFieldStringProperty(formTemplate, fieldName, property, locale, args, null);
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        try {
            return LocalDateTime.parse(value);
        } catch (Exception e) {
            LOGGER.error("error reading [{}] of [{}.{}] as LocalDateTime", property, formTemplate, fieldName, e);
            return null;
        }
    }

    public static LocalTime getFieldLocalTimeProperty(FormTemplate formTemplate, String fieldName, String property, Locale locale, Map<String, String> args, LocalTime defaultValue) {
        String value = FormUtils.getFieldStringProperty(formTemplate, fieldName, property, locale, args, null);
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        try {
            return LocalTime.parse(value);
        } catch (Exception e) {
            LOGGER.error("error reading [{}] of [{}.{}] as LocalTime", property, formTemplate, fieldName, e);
            return null;
        }
    }

    /**
     * @return null if we can't read property value
     */
    public static String getFieldStringProperty(FormTemplate formTemplate, String fieldName, String property, Locale locale, Map<String, String> args, String defaultValue) {
        String value = FormUtils.getString(formTemplate.getName() + "." + fieldName + "." + property, locale, args, null, formTemplate.getMessageSource());
        if (StringUtils.isBlank(value)) {
            value = FormUtils.getString("field." + fieldName + "." + property, locale, args, defaultValue, formTemplate.getMessageSource());
        }
        return value;
    }

    public static String getString(String key, Locale locale, Map<String, String> args, String defaultValue, MessageSource messageSource) {
        String value = messageSource.getMessage(key, null, null, locale);
        if (StringUtils.isBlank(value)) {
            value = defaultValue;
        }
        return StringTemplateUtils.fillTemplate(value, args);
    }
}
