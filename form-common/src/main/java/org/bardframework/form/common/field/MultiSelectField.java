package org.bardframework.form.common.field;

import com.fasterxml.jackson.annotation.JsonTypeName;
import org.bardframework.form.common.FieldType;
import org.bardframework.form.common.FieldTypeBase;
import org.bardframework.form.common.field.base.FormField;
import org.bardframework.form.common.field.common.SelectOption;

import java.util.List;

@JsonTypeName("MULTI_SELECT")
public class MultiSelectField extends FormField<List<String>> {
    private Integer maxCount;
    private List<SelectOption> options;

    public MultiSelectField() {
    }

    public MultiSelectField(String name, List<SelectOption> options) {
        super(name);
        this.options = options;
    }

    @Override
    public FieldType<?> getType() {
        return FieldTypeBase.MULTI_SELECT;
    }

    @Override
    public String toString(List<String> value) {
        return null == value ? null : String.join(SEPARATOR, value);
    }

    public Integer getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(Integer maxCount) {
        this.maxCount = maxCount;
    }

    public List<SelectOption> getOptions() {
        return options;
    }

    public void setOptions(List<SelectOption> options) {
        this.options = options;
    }
}
