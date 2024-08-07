package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;
import org.bardframework.form.field.option.OptionDataSource;

@Getter
@Setter
public class MultiSelectFieldTemplate extends MultiSelectFieldTemplateAbstract<MultiSelectField> {

    public MultiSelectFieldTemplate(String name, OptionDataSource optionDataSource) {
        super(name, optionDataSource);
    }
}