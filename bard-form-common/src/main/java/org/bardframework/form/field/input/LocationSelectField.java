package org.bardframework.form.field.input;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bardframework.form.field.FieldType;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
public class LocationSelectField extends InputField<String> {

    private Boolean showAddressAsTitle;

    public LocationSelectField() {
    }

    public LocationSelectField(String name) {
        super(name);
    }

    @Override
    public String toString(String value) {
        return value;
    }

    @Override
    public FieldType getType() {
        return InputFieldType.LOCATION_SELECT;
    }
}
