package org.bardframework.table.header;

import org.bardframework.commons.utils.ReflectionUtils;

public class EnumPropertyPathHeaderTemplate<E extends Enum<E>> extends I18nBasedHeaderTemplate<Object, E> {

    private final String propertyPath;

    public EnumPropertyPathHeaderTemplate(String propertyPath) {
        this.propertyPath = propertyPath;
    }

    protected E getValue(Object object) {
        try {
            return (E) ReflectionUtils.getPropertyValue(object, propertyPath);
        } catch (Exception e) {
            throw new IllegalStateException(String.format("can't read property [%s] of [%s] instance and convert it.", this.getName(), object.getClass()), e);
        }
    }

    @Override
    protected String getI18nKey(E value) {
        return value.getClass().getSimpleName() + "." + value.name();
    }
}
