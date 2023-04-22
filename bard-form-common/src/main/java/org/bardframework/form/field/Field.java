package org.bardframework.form.field;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@ToString
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, property = "@type")
public abstract class Field {
    private String title;
    private String description;

    public abstract FieldType getType();

    /**
     * TODO remove after resolve polymorphic deserialization in tests
     */
    @Deprecated
    public void setType(String type) {
    }

}
