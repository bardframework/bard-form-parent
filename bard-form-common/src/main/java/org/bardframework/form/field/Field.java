package org.bardframework.form.field;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, property = "@type")
public abstract class Field {
    private String name;
    private String title;
    private String info;
    private String description;

    public abstract FieldType getType();

    public Field() {
    }

    public Field(String name) {
        this.name = name;
    }

    /**
     * TODO remove after resolve polymorphic deserialization in tests
     */
    @Deprecated
    public void setType(String type) {
    }

}
