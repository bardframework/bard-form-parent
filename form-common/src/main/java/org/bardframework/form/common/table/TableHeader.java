package org.bardframework.form.common.table;

public class TableHeader {
    private String name;
    private String title;
    private Boolean hidden;
    private Boolean sortable;
    private Boolean movable;
    private Boolean sticky;

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

    public Boolean getHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    public Boolean getSortable() {
        return sortable;
    }

    public void setSortable(Boolean sortable) {
        this.sortable = sortable;
    }

    public Boolean getMovable() {
        return movable;
    }

    public void setMovable(Boolean movable) {
        this.movable = movable;
    }

    public Boolean getSticky() {
        return sticky;
    }

    public void setSticky(Boolean sticky) {
        this.sticky = sticky;
    }
}
