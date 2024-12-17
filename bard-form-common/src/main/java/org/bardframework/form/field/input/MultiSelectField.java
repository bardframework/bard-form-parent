package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bardframework.form.field.FieldType;
import org.bardframework.form.model.SelectOption;

import java.util.List;

@Getter
@Setter
@ToString
public class MultiSelectField extends InputField<List<String>> {
    private Integer maxCount;
    private List<SelectOption> options;

    public MultiSelectField() {
    }

    public MultiSelectField(String name, List<SelectOption> options) {
        super(name);
        this.options = options;
    }

    @Override
    public FieldType getType() {
        return InputFieldType.MULTI_SELECT;
    }

}
