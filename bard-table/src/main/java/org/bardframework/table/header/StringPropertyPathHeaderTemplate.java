package org.bardframework.table.header;

import lombok.SneakyThrows;
import org.bardframework.commons.utils.ReflectionUtils;
import org.springframework.context.MessageSource;

import java.util.Locale;

public class StringPropertyPathHeaderTemplate extends HeaderTemplate<Object, StringHeader, String> {

    private final String propertyPath;

    public StringPropertyPathHeaderTemplate(String propertyPath) {
        this.propertyPath = propertyPath;
    }

    @SneakyThrows
    @Override
    public Object getValue(Object object, MessageSource messageSource, Locale locale, boolean export) {
        return ReflectionUtils.getPropertyValue(object, propertyPath);
    }

    @Override
    public StringHeader getEmptyHeader() {
        return new StringHeader();
    }
}
