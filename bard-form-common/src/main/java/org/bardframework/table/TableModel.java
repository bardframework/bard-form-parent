package org.bardframework.table;

import org.bardframework.form.BardForm;
import org.bardframework.table.header.TableHeader;

import java.util.ArrayList;
import java.util.List;

public class TableModel {
    private String name;
    private String title;
    private String description;
    private Integer fetchSize;

    private List<TableHeader> headers;

    private BardForm filterForm;
    private BardForm saveForm;
    private BardForm updateForm;

    private Boolean delete;
    private Boolean print;
    private Boolean export;
    private Boolean preload;
    private Boolean pageable;
    private Boolean hideColumn;

    public void addHeader(TableHeader header) {
        if (null == this.headers) {
            this.headers = new ArrayList<>();
        }
        this.headers.add(header);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getFetchSize() {
        return fetchSize;
    }

    public void setFetchSize(Integer fetchSize) {
        this.fetchSize = fetchSize;
    }

    public List<TableHeader> getHeaders() {
        return headers;
    }

    public void setHeaders(List<TableHeader> headers) {
        this.headers = headers;
    }

    public BardForm getFilterForm() {
        return filterForm;
    }

    public void setFilterForm(BardForm filterForm) {
        this.filterForm = filterForm;
    }

    public BardForm getSaveForm() {
        return saveForm;
    }

    public void setSaveForm(BardForm saveForm) {
        this.saveForm = saveForm;
    }

    public BardForm getUpdateForm() {
        return updateForm;
    }

    public void setUpdateForm(BardForm updateForm) {
        this.updateForm = updateForm;
    }

    public Boolean getDelete() {
        return delete;
    }

    public void setDelete(Boolean delete) {
        this.delete = delete;
    }

    public Boolean getPrint() {
        return print;
    }

    public void setPrint(Boolean print) {
        this.print = print;
    }

    public Boolean getExport() {
        return export;
    }

    public void setExport(Boolean export) {
        this.export = export;
    }

    public Boolean getPreload() {
        return preload;
    }

    public void setPreload(Boolean preload) {
        this.preload = preload;
    }

    public Boolean getPageable() {
        return pageable;
    }

    public void setPageable(Boolean pageable) {
        this.pageable = pageable;
    }

    public Boolean getHideColumn() {
        return hideColumn;
    }

    public void setHideColumn(Boolean hideColumn) {
        this.hideColumn = hideColumn;
    }
}
