package org.bardframework.form.common.field.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.bardframework.form.common.FieldType;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public abstract class Field {
    private String label;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @JsonIgnore
    public abstract FieldType<?> getType();

}
