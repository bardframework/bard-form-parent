package org.bardframework.form.common.field;

import com.fasterxml.jackson.annotation.JsonTypeName;
import org.bardframework.form.common.FieldType;
import org.bardframework.form.common.FieldTypeBase;
import org.bardframework.form.common.field.base.FormField;
import org.bardframework.form.common.field.common.SelectOption;

import java.util.List;

@JsonTypeName("SINGLE_SELECT")
public class SingleSelectField extends FormField<String> {
    private List<SelectOption> options;

    public SingleSelectField() {
    }

    public SingleSelectField(String name, List<SelectOption> options) {
        super(name);
        this.options = options;
    }

    @Override
    public String toString(String value) {
        return value;
    }

    @Override
    public FieldType<?> getType() {
        return FieldTypeBase.SINGLE_SELECT;
    }

    public List<SelectOption> getOptions() {
        return options;
    }

    public void setOptions(List<SelectOption> options) {
        this.options = options;
    }

}
