package org.bardframework.form.field.input;

import lombok.*;
import org.bardframework.form.field.FieldType;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder
public class MultiLocaleSelectField extends MultiSelectSearchableField {

    private List<String> availableLocales;
    private List<String> excludeLocales;

    @Override
    public FieldType getType() {
        return InputFieldType.MULTI_LOCALE_SELECT;
    }
}
