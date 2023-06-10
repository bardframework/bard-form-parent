package org.bardframework.table.header;

public class EnumHeaderTemplate<M, T extends Enum<T>> extends I18nBasedHeaderTemplate<M, T> {

    @Override
    protected String getI18nKey(T value) {
        return value.getClass().getSimpleName() + "." + value.name();
    }
}
