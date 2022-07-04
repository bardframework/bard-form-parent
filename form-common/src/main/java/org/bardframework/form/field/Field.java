package org.bardframework.form.field;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, property = "@type")
public abstract class Field {
    private String title;
    private String description;

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

    public abstract FieldType getType();

    /**
     * TODO remove after resolve polymorphic deserialization in tests
     */
    @Deprecated
    public void setType(String type) {
    }

}
