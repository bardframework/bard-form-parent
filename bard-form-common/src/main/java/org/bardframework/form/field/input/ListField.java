package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bardframework.form.field.FieldType;

import java.util.List;

@Getter
@Setter
@ToString
public class ListField extends InputField<List<String>> {
    private Integer minLength;
    private Integer maxLength;
    private Integer maxCount;
    private Boolean bulkAdd;

    public ListField() {
    }

    public ListField(String name) {
        super(name);
    }

    @Override
    public FieldType getType() {
        return InputFieldType.LIST;
    }

}