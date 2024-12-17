package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bardframework.form.field.FieldType;

@Getter
@Setter
@ToString
public class IconSelectField extends InputField<String> {

    @Override
    public FieldType getType() {
        return InputFieldType.ICON_SELECT;
    }

}
