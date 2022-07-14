package org.bardframework.form.table;

import org.bardframework.form.Form;
import org.bardframework.form.table.header.TableHeader;

import java.util.ArrayList;
import java.util.List;

public class TableModel {
    private String name;
    private String title;
    private String hint;
    private List<TableHeader> headers;
    private Form filterForm;
    private Form saveForm;
    private Form updateForm;
    private Boolean delete;
    private Boolean print;
    private Boolean export;
    private Boolean preload;
    private Boolean pageable;
    private Integer fetchSize;

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

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public List<TableHeader> getHeaders() {
        return headers;
    }

    public void setHeaders(List<TableHeader> headers) {
        this.headers = headers;
    }

    public Form getFilterForm() {
        return filterForm;
    }

    public void setFilterForm(Form filterForm) {
        this.filterForm = filterForm;
    }

    public Form getSaveForm() {
        return saveForm;
    }

    public void setSaveForm(Form saveForm) {
        this.saveForm = saveForm;
    }

    public Form getUpdateForm() {
        return updateForm;
    }

    public void setUpdateForm(Form updateForm) {
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

    public Integer getFetchSize() {
        return fetchSize;
    }

    public void setFetchSize(Integer fetchSize) {
        this.fetchSize = fetchSize;
    }
}
