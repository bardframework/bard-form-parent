package org.bardframework.form;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bardframework.commons.utils.AssertionUtils;
import org.bardframework.commons.utils.StringTemplateUtils;
import org.bardframework.form.field.FieldTemplate;
import org.springframework.context.MessageSource;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@UtilityClass
public class FormUtils {

    public static BardForm toForm(FormTemplate formTemplate, Map<String, Object> values, Map<String, Object> args, Locale locale, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
        if (null == formTemplate) {
            return null;
        }
        BardForm form = new BardForm();
        formTemplate.fillForm(form, values, args, locale, httpRequest, httpResponse);
        return form;
    }

    /**
     * @return null if we can't find
     */
    public static Integer getFormIntegerProperty(FormTemplate formTemplate, String property, Locale locale, Map<String, Object> args, Integer defaultValue) {
        String value = FormUtils.getFormStringProperty(formTemplate, property, locale, args, null);
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            log.error("error reading [{}] of form [{}] as Integer", property, formTemplate.getName(), e);
            return null;
        }
    }

    /**
     * @return false if we can't find
     */
    public static Boolean getFormBooleanProperty(FormTemplate formTemplate, String property, Locale locale, Map<String, Object> args, Boolean defaultValue) {
        String value = FormUtils.getFormStringProperty(formTemplate, property, locale, args, null);
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        try {
            return Boolean.parseBoolean(value);
        } catch (Exception e) {
            log.error("error reading [{}] of form [{}] as boolean", property, formTemplate.getName(), e);
            return null;
        }
    }

    public static <T extends Enum<T>> T getFormEnumProperty(FormTemplate formTemplate, String property, Class<T> enumClass, Locale locale, Map<String, Object> args, T defaultValue) {
        String enumName = FormUtils.getFormStringProperty(formTemplate, property, locale, args, null);
        if (StringUtils.isBlank(enumName)) {
            return defaultValue;
        }
        for (final T each : enumClass.getEnumConstants()) {
            if (each.name().equalsIgnoreCase(enumName)) {
                return each;
            }
        }
        log.error("error reading [{}] of form[{}] as Enum[{}]", property, formTemplate, enumClass);
        return null;
    }

    public static String getFormStringProperty(FormTemplate formTemplate, String property, Locale locale, Map<String, Object> args, String defaultValue) {
        return FormUtils.getFormStringProperty(formTemplate.getName(), property, locale, args, defaultValue, formTemplate.getMessageSource());
    }

    public static String getFormStringProperty(String formName, String property, Locale locale, Map<String, Object> args, String defaultValue, MessageSource messageSource) {
        return FormUtils.getString("form", property, List.of(formName), locale, args, defaultValue, messageSource);
    }

    public static Boolean getFieldBooleanProperty(FormTemplate formTemplate, FieldTemplate<?> fieldTemplate, String property, Locale locale, Map<String, Object> args, Boolean defaultValue) {
        return FormUtils.getFieldBooleanProperty(formTemplate, fieldTemplate.getName(), property, locale, args, defaultValue);
    }

    /**
     * @return null if we can't read property value
     */
    public static Boolean getFieldBooleanProperty(FormTemplate FormTemplate, String fieldName, String property, Locale locale, Map<String, Object> args, Boolean defaultValue) {
        String value = FormUtils.getFieldStringProperty(FormTemplate, fieldName, property, locale, args, null);
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        try {
            return Boolean.parseBoolean(value);
        } catch (Exception e) {
            log.error("error reading [{}] of [{}.{}] as boolean", property, FormTemplate, fieldName, e);
            return null;
        }
    }

    public static List<String> getFieldListProperty(FormTemplate formTemplate, FieldTemplate<?> fieldTemplate, String property, Locale locale, Map<String, Object> args, List<String> defaultValue) {
        return FormUtils.getFieldListProperty(formTemplate, fieldTemplate.getName(), property, locale, args, defaultValue);
    }

    /**
     * @return defaultValue if we can't read property value
     */
    public static List<String> getFieldListProperty(FormTemplate formTemplate, String fieldName, String property, Locale locale, Map<String, Object> args, List<String> defaultValue) {
        String value = FormUtils.getFieldStringProperty(formTemplate, fieldName, property, locale, args, null);
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        return Arrays.stream(value.split(",")).map(String::trim).collect(Collectors.toList());
    }

    public static Integer getFieldIntegerProperty(FormTemplate formTemplate, FieldTemplate<?> fieldTemplate, String property, Locale locale, Map<String, Object> args, Integer defaultValue) {
        return FormUtils.getFieldIntegerProperty(formTemplate, fieldTemplate.getName(), property, locale, args, defaultValue);
    }

    /**
     * @return null if we can't read property value
     */
    public static Integer getFieldIntegerProperty(FormTemplate formTemplate, String fieldName, String property, Locale locale, Map<String, Object> args, Integer defaultValue) {
        String value = FormUtils.getFieldStringProperty(formTemplate, fieldName, property, locale, args, null);
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            log.error("error reading [{}] of [{}.{}] as Integer", property, formTemplate, fieldName, e);
            return null;
        }
    }

    public static Double getFieldDoubleProperty(FormTemplate formTemplate, FieldTemplate<?> fieldTemplate, String property, Locale locale, Map<String, Object> args, Double defaultValue) {
        return FormUtils.getFieldDoubleProperty(formTemplate, fieldTemplate.getName(), property, locale, args, defaultValue);
    }

    /**
     * @return null if we can't read property value
     */
    public static Double getFieldDoubleProperty(FormTemplate formTemplate, String fieldName, String property, Locale locale, Map<String, Object> args, Double defaultValue) {
        String value = FormUtils.getFieldStringProperty(formTemplate, fieldName, property, locale, args, null);
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            log.error("error reading [{}] of [{}.{}] as Double", property, formTemplate, fieldName, e);
            return null;
        }
    }

    public static Byte getFieldByteProperty(FormTemplate formTemplate, FieldTemplate<?> fieldTemplate, String property, Locale locale, Map<String, Object> args, Byte defaultValue) {
        return FormUtils.getFieldByteProperty(formTemplate, fieldTemplate.getName(), property, locale, args, defaultValue);
    }

    /**
     * @return null if we can't read property value
     */
    public static Byte getFieldByteProperty(FormTemplate formTemplate, String fieldName, String property, Locale locale, Map<String, Object> args, Byte defaultValue) {
        String value = FormUtils.getFieldStringProperty(formTemplate, fieldName, property, locale, args, null);
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        try {
            return Byte.parseByte(value);
        } catch (Exception e) {
            log.error("error reading [{}] of [{}.{}] as Byte", property, formTemplate, fieldName, e);
            return null;
        }
    }

    public static Long getFieldLongProperty(FormTemplate formTemplate, FieldTemplate<?> fieldTemplate, String property, Locale locale, Map<String, Object> args, Long defaultValue) {
        return FormUtils.getFieldLongProperty(formTemplate, fieldTemplate.getName(), property, locale, args, defaultValue);
    }

    /**
     * @return null if we can't read property value
     */
    public static Long getFieldLongProperty(FormTemplate formTemplate, String fieldName, String property, Locale locale, Map<String, Object> args, Long defaultValue) {
        String value = FormUtils.getFieldStringProperty(formTemplate, fieldName, property, locale, args, null);
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        try {
            return Long.parseLong(value);
        } catch (Exception e) {
            log.error("error reading [{}] of [{}.{}] as Long", property, formTemplate, fieldName, e);
            return null;
        }
    }

    public static <T extends Enum<T>> T getFieldEnumProperty(FormTemplate formTemplate, FieldTemplate<?> fieldTemplate, String property, Class<T> enumClass, Locale locale, Map<String, Object> args, T defaultValue) {
        return FormUtils.getFieldEnumProperty(formTemplate, fieldTemplate.getName(), property, enumClass, locale, args, defaultValue);
    }

    public static <T extends Enum<T>> T getFieldEnumProperty(FormTemplate formTemplate, String fieldName, String property, Class<T> enumClass, Locale locale, Map<String, Object> args, T defaultValue) {
        String enumName = FormUtils.getFieldStringProperty(formTemplate, fieldName, property, locale, args, null);
        if (StringUtils.isBlank(enumName)) {
            return defaultValue;
        }
        for (final T each : enumClass.getEnumConstants()) {
            if (each.name().equalsIgnoreCase(enumName)) {
                return each;
            }
        }
        log.error("error reading [{}] of [{}.{}] as Enum[{}]", property, formTemplate, fieldName, enumClass);
        return null;
    }

    public static LocalTime getFieldLocalTimeProperty(FormTemplate formTemplate, FieldTemplate<?> fieldTemplate, String property, Locale locale, Map<String, Object> args, LocalTime defaultValue) {
        return FormUtils.getFieldLocalTimeProperty(formTemplate, fieldTemplate.getName(), property, locale, args, defaultValue);
    }

    public static LocalTime getFieldLocalTimeProperty(FormTemplate formTemplate, String fieldName, String property, Locale locale, Map<String, Object> args, LocalTime defaultValue) {
        String value = FormUtils.getFieldStringProperty(formTemplate, fieldName, property, locale, args, null);
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        try {
            return LocalTime.parse(value);
        } catch (Exception e) {
            log.error("error reading [{}] of [{}.{}] as LocalTime", property, formTemplate, fieldName, e);
            return null;
        }
    }

    public static String getFieldStringProperty(FormTemplate formTemplate, FieldTemplate<?> fieldTemplate, String property, Locale locale, Map<String, Object> args, String defaultValue) {
        return FormUtils.getFieldStringProperty(formTemplate, fieldTemplate.getName(), property, locale, args, defaultValue);
    }

    /**
     * @return null if we can't read property value
     */
    public static String getFieldStringProperty(FormTemplate formTemplate, String fieldName, String property, Locale locale, Map<String, Object> args, String defaultValue) {
        return FormUtils.getFieldStringProperty(formTemplate.getName(), fieldName, property, locale, args, defaultValue, formTemplate.getMessageSource());
    }

    /**
     * @return null if we can't read property value
     */
    public static String getFieldStringProperty(String formName, String fieldName, String property, Locale locale, Map<String, Object> args, String defaultValue, MessageSource messageSource) {
        return FormUtils.getString("field", property, List.of(formName, fieldName), locale, args, defaultValue, messageSource);
    }

    /**
     * 'form'.[tableName].[formName].property
     * <br>
     * 'table'.[tableName].property
     * <br>
     * 'field'.[tableName].[formName].[fieldName].property
     * <br>
     * 'header'.[tableName].[headerName].property
     *
     * @return null if we can't read property value
     */
    public static String getString(String keyType, String property, List<String> parts, Locale locale, Map<String, Object> args, String defaultValue, MessageSource messageSource) {
        AssertionUtils.isNotBlank(keyType, "keyType can't be empty");
        AssertionUtils.isNotBlank(property, "property can't be empty");
        List<String> keyParts = FormUtils.constructMessageKeys(parts);
        for (String part : keyParts) {
            String key = Stream.of(keyType, part, property).filter(StringUtils::isNotBlank).collect(Collectors.joining("."));
            String value = messageSource.getMessage(key, null, null, locale);
            if (StringUtils.isNotBlank(value)) {
                return StringTemplateUtils.fillTemplate(value, args);
            }
        }
        return StringTemplateUtils.fillTemplate(defaultValue, args);
    }

    public static List<String> constructMessageKeys(List<String> keyParts) {
        List<String> cleanParts = keyParts.stream().filter(StringUtils::isNotBlank).collect(Collectors.toList());
        List<String> keys = new ArrayList<>(FormUtils.constructKeys(cleanParts));
        keys.add("");
        return keys;
    }

    private static List<String> constructKeys(List<String> keyParts) {
        if (keyParts.isEmpty()) {
            return Collections.emptyList();
        }
        if (keyParts.size() == 1) {
            return keyParts;
        }
        String prefix = keyParts.get(0);
        List<String> subKeys = FormUtils.constructKeys(keyParts.subList(1, keyParts.size()));
        List<String> keys = subKeys.stream().map(keyPart -> String.join(".", prefix, keyPart)).collect(Collectors.toList());
        keys.add(prefix);
        keys.addAll(subKeys);
        return keys;
    }
}
