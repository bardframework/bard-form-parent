package org.bardframework.form.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SelectOption implements Comparable<SelectOption> {
    private String id;
    private String title;
    private String description;
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
