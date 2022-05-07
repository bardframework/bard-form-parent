package org.bardframework.form.common;

import org.bardframework.form.common.field.base.Field;

import java.util.ArrayList;
import java.util.List;

public class Form {

    private String id;
    private String title;
    private String hint;
    private String confirmMessage;
    private String submitLabel;
    private List<Field> fields = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
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

    public void addField(Field field) {
        if (null == this.fields) {
            this.fields = new ArrayList<>();
        }
        this.fields.add(field);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Form that = (Form) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
