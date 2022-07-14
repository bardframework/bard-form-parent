package org.bardframework.form.model;

public class SelectOption implements Comparable<SelectOption> {
    private String id;
    private String title;
    private String type;
    private String icon;
    private Boolean disable;

    public SelectOption() {
    }

    public SelectOption(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public SelectOption(String id, String title, String type) {
        this.id = id;
        this.title = title;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getDisable() {
        return disable;
    }

    public void setDisable(Boolean disable) {
        this.disable = disable;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public int compareTo(SelectOption other) {
        if (null == other || null == other.getTitle()) {
            return 1;
        }
        if (null == this.getTitle()) {
            return -1;
        }
        return this.getTitle().compareTo(other.getTitle());
    }
}
