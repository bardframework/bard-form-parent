package org.bardframework.table;

import org.apache.commons.lang3.StringUtils;
import org.bardframework.form.FormUtils;
import org.bardframework.table.header.TableHeaderTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class TableUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(TableUtils.class);

    private TableUtils() {
        /*
            prevent instantiation
         */
    }

    public static TableModel toTable(TableTemplate tableTemplate, Map<String, String> args, Locale locale, HttpServletRequest httpRequest) throws Exception {
        if (null == tableTemplate) {
            return null;
        }
        return TableUtils.toTable(new TableModel(), tableTemplate, args, locale, httpRequest);
    }

    public static <T extends TableModel> T toTable(T table, TableTemplate tableTemplate, Map<String, String> args, Locale locale, HttpServletRequest httpRequest) throws Exception {
        table.setName(tableTemplate.getName());
        table.setTitle(TableUtils.getTableStringValue(tableTemplate, "title", locale, args, tableTemplate.getTitle()));
        table.setHint(TableUtils.getTableStringValue(tableTemplate, "hint", locale, args, tableTemplate.getHint()));
        table.setDelete(TableUtils.getTableBooleanValue(tableTemplate, "delete", locale, args, tableTemplate.getDelete()));
        table.setPrint(TableUtils.getTableBooleanValue(tableTemplate, "print", locale, args, tableTemplate.getPrint()));
        table.setExport(TableUtils.getTableBooleanValue(tableTemplate, "export", locale, args, tableTemplate.getExport()));
        table.setPreload(TableUtils.getTableBooleanValue(tableTemplate, "preload", locale, args, tableTemplate.getPreload()));
        table.setPageable(TableUtils.getTableBooleanValue(tableTemplate, "pageable", locale, args, tableTemplate.getPageable()));
        table.setFetchSize(TableUtils.getTableIntegerValue(tableTemplate, "fetchSize", locale, args, tableTemplate.getFetchSize()));
        table.setFilterForm(FormUtils.toForm(tableTemplate.getFilterFormTemplate(), args, Map.of(), locale, httpRequest));
        table.setSaveForm(FormUtils.toForm(tableTemplate.getSaveFormTemplate(), args, Map.of(), locale, httpRequest));
        table.setUpdateForm(FormUtils.toForm(tableTemplate.getUpdateFormTemplate(), args, Map.of(), locale, httpRequest));

        for (TableHeaderTemplate<?, ?> headerTemplate : tableTemplate.getHeaderTemplates()) {
            table.addHeader(headerTemplate.toHeader(tableTemplate, args, locale));
        }
        return table;
    }

    /**
     * @return false if we can't find
     */
    public static Boolean getTableBooleanValue(TableTemplate tableTemplate, String property, Locale locale, Map<String, String> args, Boolean defaultValue) {
        String value = TableUtils.getTableStringValue(tableTemplate, property, locale, args, null);
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        try {
            return Boolean.parseBoolean(value);
        } catch (Exception e) {
            LOGGER.error("error reading [{}] of [{}] as boolean", property, tableTemplate.getName(), e);
            return null;
        }
    }

    /**
     * @return null if we can't read property value
     */
    public static Integer getTableIntegerValue(TableTemplate tableTemplate, String property, Locale locale, Map<String, String> args, Integer defaultValue) {
        String value = TableUtils.getTableStringValue(tableTemplate, property, locale, args, null);
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            LOGGER.error("error reading [{}] of [{}] as Integer", property, tableTemplate, e);
            return null;
        }
    }

    public static String getTableStringValue(TableTemplate tableTemplate, String property, Locale locale, Map<String, String> args, String defaultValue) {
        return TableUtils.getTableValue(tableTemplate.getName(), property, locale, args, defaultValue, tableTemplate.getMessageSource());
    }

    public static String getTableValue(String tableName, String property, Locale locale, Map<String, String> args, String defaultValue, MessageSource messageSource) {
        return FormUtils.getString("table", property, List.of(tableName), locale, args, defaultValue, messageSource);
    }

    /**
     * @return false if we can't read property value
     */
    public static Boolean getHeaderBooleanValue(TableTemplate TableTemplate, TableHeaderTemplate<?, ?> headerTemplate, String property, Locale locale, Map<String, String> args, Boolean defaultValue) {
        String value = TableUtils.getHeaderStringValue(TableTemplate, headerTemplate, property, locale, args, null);
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        try {
            return Boolean.parseBoolean(value);
        } catch (Exception e) {
            LOGGER.error("error reading [{}] of [{}.{}] as Boolean", property, TableTemplate, headerTemplate.getName(), e);
            return null;
        }
    }

    /**
     * @return false if we can't read property value
     */
    public static List<String> getHeaderListValue(TableTemplate tableTemplate, TableHeaderTemplate<?, ?> headerTemplate, String property, Locale locale, Map<String, String> args, List<String> defaultValue) {
        String value = TableUtils.getHeaderStringValue(tableTemplate, headerTemplate, property, locale, args, null);
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        return Arrays.stream(value.split(",")).map(String::trim).collect(Collectors.toList());
    }

    /**
     * @return null if we can't read property value
     */
    public static Double getHeaderDoubleValue(TableTemplate tableTemplate, TableHeaderTemplate<?, ?> headerTemplate, String property, Locale locale, Map<String, String> args, Double defaultValue) {
        String value = TableUtils.getHeaderStringValue(tableTemplate, headerTemplate, property, locale, args, null);
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            LOGGER.error("error reading [{}] of [{}.{}] as Double", property, tableTemplate, headerTemplate.getName(), e);
            return null;
        }
    }

    /**
     * @return null if we can't read property value
     */
    public static Long getHeaderLongValue(TableTemplate tableTemplate, TableHeaderTemplate<?, ?> headerTemplate, String property, Locale locale, Map<String, String> args, Long defaultValue) {
        String value = TableUtils.getHeaderStringValue(tableTemplate, headerTemplate, property, locale, args, null);
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        try {
            return Long.parseLong(value);
        } catch (Exception e) {
            LOGGER.error("error reading [{}] of [{}.{}] as Long", property, tableTemplate, headerTemplate.getName(), e);
            return null;
        }
    }

    /**
     * @return null if we can't read property value
     */
    public static LocalDate getHeaderLocalDateValue(TableTemplate tableTemplate, TableHeaderTemplate<?, ?> headerTemplate, String property, Locale locale, Map<String, String> args, LocalDate defaultValue) {
        String value = TableUtils.getHeaderStringValue(tableTemplate, headerTemplate, property, locale, args, null);
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        try {
            return LocalDate.parse(value);
        } catch (Exception e) {
            LOGGER.error("error reading [{}] of [{}.{}] as LocalDate", property, tableTemplate, headerTemplate.getName(), e);
            return null;
        }
    }

    public static LocalDateTime getHeaderLocalDateTimeValue(TableTemplate tableTemplate, TableHeaderTemplate<?, ?> headerTemplate, String property, Locale locale, Map<String, String> args, LocalDateTime defaultValue) {
        String value = TableUtils.getHeaderStringValue(tableTemplate, headerTemplate, property, locale, args, null);
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        try {
            return LocalDateTime.parse(value);
        } catch (Exception e) {
            LOGGER.error("error reading [{}] of [{}.{}] as LocalDateTime", property, tableTemplate, headerTemplate.getName(), e);
            return null;
        }
    }

    public static LocalTime getHeaderLocalTimeValue(TableTemplate tableTemplate, TableHeaderTemplate<?, ?> headerTemplate, String property, Locale locale, Map<String, String> args, LocalTime defaultValue) {
        String value = TableUtils.getHeaderStringValue(tableTemplate, headerTemplate, property, locale, args, null);
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        try {
            return LocalTime.parse(value);
        } catch (Exception e) {
            LOGGER.error("error reading [{}] of [{}.{}] as LocalTime", property, tableTemplate, headerTemplate.getName(), e);
            return null;
        }
    }

    /**
     * @return null if we can't read property value
     */
    public static String getHeaderStringValue(TableTemplate tableTemplate, TableHeaderTemplate<?, ?> headerTemplate, String property, Locale locale, Map<String, String> args, String defaultValue) {
        return TableUtils.getHeaderStringValue(tableTemplate.getName(), headerTemplate, property, locale, args, defaultValue, tableTemplate.getMessageSource());
    }

    /**
     * @return null if we can't read property value
     */
    public static String getHeaderStringValue(String tableName, TableHeaderTemplate<?, ?> headerTemplate, String property, Locale locale, Map<String, String> args, String defaultValue, MessageSource messageSource) {
        return FormUtils.getString("header", property, List.of(tableName, headerTemplate.getName()), locale, args, defaultValue, messageSource);
    }
}
