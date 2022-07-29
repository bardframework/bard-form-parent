package org.bardframework.table.header;

import org.apache.commons.lang3.EnumUtils;
import org.springframework.context.MessageSource;

import java.util.Locale;

public class EnumHeaderTemplate<T extends Enum<T>> extends HeaderTemplate<StringHeader, T> {
    private final Class<T> enumClass;

    public EnumHeaderTemplate(Class<T> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public T parse(String value, Locale locale) {
        return EnumUtils.getEnum(enumClass, value, null);
    }

    @Override
    public Object format(T value, Locale locale, MessageSource messageSource) {
        if (null == value) {
            return null;
        }
        String titleMessageKey = value.getClass().getSimpleName() + "." + value.name();
        return messageSource.getMessage(titleMessageKey, new Object[0], titleMessageKey, locale);
    }
}
