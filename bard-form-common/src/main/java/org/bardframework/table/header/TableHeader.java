package org.bardframework.table.header;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bardframework.table.header.type.HeaderType;

@Getter
@Setter
@ToString
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, property = "@type")
public abstract class TableHeader {
    private String name;
    private String title;
    private String description;
    private Boolean hidden;
    private Boolean sortable;
    private Boolean movable;
    private Boolean sticky;

    public abstract HeaderType getType();

    /**
     * TODO remove after resolve polymorphic deserialization in tests
     */
    @Deprecated
    public void setType(String type) {
    }
}
