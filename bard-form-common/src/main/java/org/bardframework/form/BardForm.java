package org.bardframework.form;

import org.bardframework.form.field.Field;

import java.util.ArrayList;
import java.util.List;

public class BardForm {

    protected String name;
    protected String title;
    protected String description;
    protected String confirmMessage;
    protected String submitLabel;
    protected FieldDescriptionShowType fieldDescriptionShowType;
    protected List<Field> fields = new ArrayList<>();

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getConfirmMessage() {
        return confirmMessage;
    }

    public void setConfirmMessage(String confirmMessage) {
        this.confirmMessage = confirmMessage;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public String getSubmitLabel() {
        return submitLabel;
    }

    public void setSubmitLabel(String submitLabel) {
        this.submitLabel = submitLabel;
    }

    public FieldDescriptionShowType getFieldDescriptionShowType() {
        return fieldDescriptionShowType;
    }

    public void setFieldDescriptionShowType(FieldDescriptionShowType fieldDescriptionShowType) {
        this.fieldDescriptionShowType = fieldDescriptionShowType;
    }

    public void addField(Field field) {
        if (null == this.fields) {
            this.fields = new ArrayList<>();
        }
        this.fields.add(field);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BardForm that = (BardForm) o;

        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
