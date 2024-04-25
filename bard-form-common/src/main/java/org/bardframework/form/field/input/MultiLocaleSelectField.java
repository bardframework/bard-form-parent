package org.bardframework.form.field.input;

import lombok.*;
import org.bardframework.form.field.FieldType;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder
public class MultiLocaleSelectField extends InputField<List<String>> {
    private Integer maxCount;
    private List<String> availableLocales;
    private List<String> excludeLocales;

    @Override
    public String toString(List<String> value) {
        return null == value ? null : String.join(SEPARATOR, value);
    }

    @Override
    public FieldType getType() {
        return InputFieldType.MULTI_LOCALE_SELECT;
    }
}
