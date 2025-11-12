package org.bardframework.table.header;

import lombok.SneakyThrows;
import org.apache.commons.lang3.math.NumberUtils;
import org.bardframework.commons.utils.ReflectionUtils;
import org.springframework.context.MessageSource;

import java.util.Locale;

public class FileSizePropertyPathHeaderTemplate extends HeaderTemplate<Object, StringHeader, String> {

    private final String propertyPath;

    public FileSizePropertyPathHeaderTemplate(String propertyPath) {
        this.propertyPath = propertyPath;
    }

    @SneakyThrows
    @Override
    public Object getValue(Object object, MessageSource messageSource, Locale locale, boolean export) {
        Object value = ReflectionUtils.getPropertyValue(object, propertyPath);
        if (value == null) {
            return null;
        }
        long bytes = NumberUtils.toLong(value.toString());
        if (bytes < 1024) {
            return bytes + " B";
        }

        double kb = bytes / 1024.0;
        if (kb < 1024) {
            return String.format("%.2f KB", kb);
        }

        double mb = kb / 1024.0;
        if (mb < 1024) {
            return String.format("%.2f MB", mb);
        }

        double gb = mb / 1024.0;
        if (gb < 1024) {
            return String.format("%.2f GB", gb);
        }

        double tb = gb / 1024.0;
        if (tb < 1024) {
            return String.format("%.2f TB", tb);
        }

        double pb = tb / 1024.0;
        if (pb < 1024) {
            return String.format("%.2f PB", pb);
        }

        double eb = pb / 1024.0;
        return String.format("%.2f EB", eb);
    }

    @Override
    public StringHeader getEmptyHeader() {
        return new StringHeader();
    }
}
