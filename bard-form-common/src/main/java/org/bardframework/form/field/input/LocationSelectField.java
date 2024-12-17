package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bardframework.form.field.FieldType;

@Getter
@Setter
@ToString
public class LocationSelectField extends InputField<String> {

    private Boolean showAddressAsTitle;

    public LocationSelectField() {
    }

    public LocationSelectField(String name) {
        super(name);
    }

    @Override
    public FieldType getType() {
        return InputFieldType.LOCATION_SELECT;
    }
}
