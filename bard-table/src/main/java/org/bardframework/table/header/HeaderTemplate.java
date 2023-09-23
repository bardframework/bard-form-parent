package org.bardframework.table.header;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.bardframework.commons.utils.ReflectionUtils;
import org.bardframework.table.TableTemplate;
import org.bardframework.table.TableUtils;
import org.bardframework.table.header.type.HeaderType;
import org.springframework.context.MessageSource;

import java.util.Locale;
import java.util.Map;

@Slf4j
@Getter
@Setter
@ToString
public abstract class HeaderTemplate<M, H extends TableHeader, T> extends TableHeader {

    private String excelFormat;

    public H toHeader(TableTemplate tableTemplate, Map<String, String> args, Locale locale) {
        H header = this.getEmptyHeader();
        header.setName(this.getName());
        this.fill(tableTemplate, header, args, locale);
        return header;
    }

    protected void fill(TableTemplate tableTemplate, H header, Map<String, String> args, Locale locale) {
        header.setTitle(TableUtils.getHeaderStringValue(tableTemplate, this, "title", locale, args, this.getName()));
        header.setDescription(TableUtils.getHeaderStringValue(tableTemplate, this, "description", locale, args, this.getName()));
        header.setHidden(TableUtils.getHeaderBooleanValue(tableTemplate, this, "hidden", locale, args, this.getHidden()));
        header.setSortable(TableUtils.getHeaderBooleanValue(tableTemplate, this, "sortable", locale, args, this.getSortable()));
        header.setMovable(TableUtils.getHeaderBooleanValue(tableTemplate, this, "movable", locale, args, this.getMovable()));
        header.setSticky(TableUtils.getHeaderBooleanValue(tableTemplate, this, "sticky", locale, args, this.getSticky()));
    }

    public abstract H getEmptyHeader();

    @Override
    public HeaderType getType() {
        return null;
    }

    public Object getValue(M model, MessageSource messageSource, Locale locale, boolean export) {
        T value = this.getValue(model);
        if (null == value) {
            return null;
        }
        if (export) {
            return this.formatForExport(value, messageSource, locale);
        } else {
            return this.format(value, messageSource, locale);
        }
    }

    protected T getValue(M model) {
        try {
            return (T) ReflectionUtils.getPropertyValue(model, this.getName());
        } catch (Exception e) {
            throw new IllegalStateException(String.format("can't read property [%s] of [%s] instance and convert it.", this.getName(), model.getClass()), e);
        }
    }

    protected Object format(T value, MessageSource messageSource, Locale locale) {
        return value;
    }

    protected Object formatForExport(T value, MessageSource messageSource, Locale locale) {
        return this.format(value, messageSource, locale);
    }
}
