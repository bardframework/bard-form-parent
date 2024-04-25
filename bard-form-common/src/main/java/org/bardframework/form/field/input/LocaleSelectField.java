package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.bardframework.form.field.FieldType;

import java.util.List;

@Slf4j
@Getter
@Setter
@ToString
public class LocaleSelectField extends InputField<String> {
    private Integer maxCount;
    private List<String> availableLocales;
    private List<String> excludeLocales;

    public LocaleSelectField() {
    }

    public LocaleSelectField(String name) {
        super(name);
    }

    @Override
    public String toString(String value) {
        return value;
    }

    @Override
    public FieldType getType() {
        return InputFieldType.LOCALE_SELECT;
    }
}
