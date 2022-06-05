package org.bardframework.form.common.field.base;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.bardframework.form.common.FieldType;

@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, property = "@type")
public abstract class Field {
    private String label;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public abstract FieldType<?> getType();

    /**
     * TODO remove after resolve polymorphic deserialization in tests
     *
     * @param type
     */
    @Deprecated
    public void setType(String type) {
    }

}
