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
        return FormUtils.toForm(new Form(), formTemplate, locale, args, values);
    }

    public static Form toForm(FormTemplate formTemplate, Locale locale, Map<String, String> args) throws Exception {
        return FormUtils.toForm(new Form(), formTemplate, locale, args, Map.of());
    }

    public static <F extends Form> F toForm(F form, FormTemplate formTemplate, Locale locale, Map<String, String> args) throws Exception {
        return FormUtils.toForm(form, formTemplate, locale, args, Map.of());
    }

    public static <F extends Form, T> F toForm(F form, FormTemplate formTemplate, Locale locale, Map<String, String> args, Map<String, String> values) throws Exception {
        form.setId(formTemplate.getName());
        form.setTitle(FormUtils.getFormValue(formTemplate, "name", locale, args));
        form.setHint(FormUtils.getFormValue(formTemplate, "description", locale, args));
        form.setConfirmMessage(FormUtils.getFormValue(formTemplate, "confirmMessage", locale, args));
        for (FieldTemplate<?> fieldTemplate : formTemplate.getFields()) {
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

    public static String getFormValue(FormTemplate formTemplate, String property, Locale locale, Map<String, String> args) {
        return FormUtils.getFormValue(formTemplate.getName(), property, locale, args, formTemplate.getMessageSource());
    }

    public static String getFormValue(String formName, String property, Locale locale, Map<String, String> args, MessageSource messageSource) {
        String value = FormUtils.getString(formName + "." + property, locale, args, messageSource);
        if (null == value) {
            value = FormUtils.getString("form." + property, locale, args, messageSource);
        }
        return StringTemplateUtils.fillTemplate(value, args);
    }

    /**
     * @return false if we can't find
     */
    public static boolean getFormBooleanValue(FormTemplate formTemplate, String property, Locale locale, Map<String, String> args) {
        String value = FormUtils.getFormValue(formTemplate, property, locale, args);
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
     * @return 0 if we can't read property value
     */
    public static int getFormIntValue(FormTemplate formTemplate, String property, Locale locale, Map<String, String> args) {
        String value = FormUtils.getFormValue(formTemplate, property, locale, args);
        if (StringUtils.isBlank(value)) {
            return 0;
        }
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            LOGGER.error("error reading [{}] of [{}] as int", property, formTemplate, e);
            return 0;
        }
    }

    /**
     * @return null if we can't read property value
     */
    public static String getFieldValue(FormTemplate formTemplate, String fieldName, String property, Locale locale, Map<String, String> args) {
        String value = FormUtils.getString(formTemplate.getName() + "." + fieldName + "." + property, locale, args, formTemplate.getMessageSource());
        if (null == value) {
            value = FormUtils.getString("field." + fieldName + "." + property, locale, args, formTemplate.getMessageSource());
        }
        return value;
    }

    /**
     * @return false if we can't read property value
     */
    public static Boolean getBooleanValue(FormTemplate FormTemplate, String fieldName, String property, Locale locale, Map<String, String> args) {
        String value = FormUtils.getFieldValue(FormTemplate, fieldName, property, locale, args);
        if (StringUtils.isBlank(value)) {
            return null;
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
    public static List<String> getListValue(FormTemplate formTemplate, String fieldName, String property, Locale locale, Map<String, String> args) {
        String value = FormUtils.getFieldValue(formTemplate, fieldName, property, locale, args);
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return Arrays.stream(value.split(",")).map(String::trim).collect(Collectors.toList());
    }

    /**
     * @return null if we can't read property value
     */
    public static Integer getIntegerValue(FormTemplate formTemplate, String fieldName, String property, Locale locale, Map<String, String> args) {
        String value = FormUtils.getFieldValue(formTemplate, fieldName, property, locale, args);
        if (StringUtils.isBlank(value)) {
            return null;
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
    public static Double getDoubleValue(FormTemplate formTemplate, String fieldName, String property, Locale locale, Map<String, String> args) {
        String value = FormUtils.getFieldValue(formTemplate, fieldName, property, locale, args);
        if (StringUtils.isBlank(value)) {
            return null;
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
    public static Long getLongValue(FormTemplate formTemplate, String fieldName, String property, Locale locale, Map<String, String> args) {
        String value = FormUtils.getFieldValue(formTemplate, fieldName, property, locale, args);
        if (StringUtils.isBlank(value)) {
            return null;
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
    public static LocalDate getLocalDateValue(FormTemplate formTemplate, String fieldName, String property, Locale locale, Map<String, String> args) {
        String value = FormUtils.getFieldValue(formTemplate, fieldName, property, locale, args);
        if (StringUtils.isBlank(value)) {
            return null;
        }
        try {
            return LocalDate.parse(value);
        } catch (Exception e) {
            LOGGER.error("error reading [{}] of [{}.{}] as LocalDate", property, formTemplate, fieldName, e);
            return null;
        }
    }

    public static String getString(String key, Locale locale, Map<String, String> args, MessageSource messageSource) {
        String value = messageSource.getMessage(key, null, null, locale);
        return StringTemplateUtils.fillTemplate(value, args);
    }
}
