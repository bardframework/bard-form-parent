package org.bardframework.form.table.header;

import org.bardframework.commons.utils.ReflectionUtils;
import org.bardframework.form.common.table.TableHeader;
import org.bardframework.form.table.TableTemplate;
import org.bardframework.form.table.TableUtils;

import java.util.Locale;
import java.util.Map;

public abstract class TableHeaderTemplate<H extends TableHeader, T> extends TableHeader {
    private final Class<H> headerClazz;
    private String excelFormat;

    protected TableHeaderTemplate() {
        this.headerClazz = ReflectionUtils.getGenericArgType(this.getClass(), 0);
    }

    public H toHeader(TableTemplate tableTemplate, Map<String, String> args, Locale locale) throws Exception {
        H header = this.getEmptyHeader();
        header.setName(this.getName());
        this.fill(tableTemplate, header, args, locale);
        return header;
    }

    protected void fill(TableTemplate tableTemplate, H header, Map<String, String> args, Locale locale) throws Exception {
        header.setTitle(TableUtils.getHeaderStringValue(tableTemplate, this.getName(), "title", locale, args, this.getName()));
        header.setHidden(TableUtils.getHeaderBooleanValue(tableTemplate, this.getName(), "hidden", locale, args, this.getHidden()));
        header.setSortable(TableUtils.getHeaderBooleanValue(tableTemplate, this.getName(), "sortable", locale, args, this.getSortable()));
        header.setMovable(TableUtils.getHeaderBooleanValue(tableTemplate, this.getName(), "movable", locale, args, this.getMovable()));
        header.setSticky(TableUtils.getHeaderBooleanValue(tableTemplate, this.getName(), "sticky", locale, args, this.getSticky()));
    }

    public H getEmptyHeader() {
        return ReflectionUtils.newInstance(headerClazz);
    }

    public abstract T toValue(String value);

    public abstract Object format(T value);

    public String getExcelFormat() {
        return excelFormat;
    }

    public void setExcelFormat(String excelFormat) {
        this.excelFormat = excelFormat;
    }
}
