package org.bardframework.table.header;

import org.apache.commons.lang3.EnumUtils;
import org.bardframework.form.table.header.StringHeader;

import java.util.Locale;

public class EnumHeaderTemplate<T extends Enum<T>> extends TableHeaderTemplate<StringHeader, T> {
    private final Class<T> enumClass;

    public EnumHeaderTemplate(Class<T> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public T parse(String value, Locale locale) {
        return EnumUtils.getEnum(enumClass, value, null);
    }

    @Override
    public Object format(T value, Locale locale) {
        return null == value ? null : value.name();
    }
}
