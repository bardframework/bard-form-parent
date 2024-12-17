package org.bardframework.form.field.input;

import org.bardframework.form.field.FieldType;

public class TimeZoneSelectField extends CountrySelectField {
    @Override
    public FieldType getType() {
        return InputFieldType.TIME_ZONE_SELECT;
    }
}