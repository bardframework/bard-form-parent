package org.bardframework.form.field.input;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bardframework.form.field.FieldType;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
public class LocaleSelectField extends InputField<List<String>> {
    private Integer maxCount;
    private List<String> availableLocales;
    private List<String> excludeLocales;

    public LocaleSelectField() {
    }

    public LocaleSelectField(String name) {
        super(name);
    }

    @Override
    public String toString(List<String> value) {
        return null == value ? null : String.join(SEPARATOR, value);
    }

    @Override
    public FieldType getType() {
        return InputFieldType.LOCALE_SELECT;
    }
}
