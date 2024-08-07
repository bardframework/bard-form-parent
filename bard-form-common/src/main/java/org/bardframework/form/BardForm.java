package org.bardframework.form;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bardframework.form.field.Field;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class BardForm {

    protected String name;
    protected String title;
    protected String description;
    protected String confirmMessage;
    protected String submitLabel;
    protected Boolean submitPristineInputs;
    protected Boolean submitEmptyInputs;
    protected NestedFormShowType nestedFormShowType;
    protected Integer autoSubmitDelaySeconds;
    protected FieldDescriptionShowType fieldDescriptionShowType;
    protected final List<Field> fields = new ArrayList<>();
    protected final List<BardForm> forms = new ArrayList<>();

    public void addField(Field field) {
        this.fields.add(field);
    }

    public void addForm(BardForm form) {
        this.forms.add(form);
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
