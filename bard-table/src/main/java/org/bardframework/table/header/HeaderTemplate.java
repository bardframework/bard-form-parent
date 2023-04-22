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
public abstract class HeaderTemplate<H extends TableHeader, T> extends TableHeader {

    private final Class<H> headerClazz;
    private String excelFormat;

    protected HeaderTemplate() {
        this.headerClazz = ReflectionUtils.getGenericArgType(this.getClass(), 0);
    }

    public H toHeader(TableTemplate tableTemplate, Map<String, String> args, Locale locale) throws Exception {
        H header = this.getEmptyHeader();
        header.setName(this.getName());
        this.fill(tableTemplate, header, args, locale);
        return header;
    }

    protected void fill(TableTemplate tableTemplate, H header, Map<String, String> args, Locale locale) throws Exception {
        header.setTitle(TableUtils.getHeaderStringValue(tableTemplate, this, "title", locale, args, this.getName()));
        header.setDescription(TableUtils.getHeaderStringValue(tableTemplate, this, "description", locale, args, this.getName()));
        header.setHidden(TableUtils.getHeaderBooleanValue(tableTemplate, this, "hidden", locale, args, this.getHidden()));
        header.setSortable(TableUtils.getHeaderBooleanValue(tableTemplate, this, "sortable", locale, args, this.getSortable()));
        header.setMovable(TableUtils.getHeaderBooleanValue(tableTemplate, this, "movable", locale, args, this.getMovable()));
        header.setSticky(TableUtils.getHeaderBooleanValue(tableTemplate, this, "sticky", locale, args, this.getSticky()));
    }

    public H getEmptyHeader() {
        return ReflectionUtils.newInstance(headerClazz);
    }

    @Override
    public HeaderType getType() {
        return null;
    }

    public abstract Object format(T value, Locale locale, MessageSource messageSource);

    public Object formatForExport(T value, Locale locale, MessageSource messageSource) {
        return this.format(value, locale, messageSource);
    }
}
