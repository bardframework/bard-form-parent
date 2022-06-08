package org.bardframework.form.filter.field;

import org.bardframework.form.common.SelectOption;
import org.bardframework.form.filter.FilterFieldType;

import java.util.List;

public class MultiSelectSearchableFilterField extends MultiSelectFilterField {

    public MultiSelectSearchableFilterField() {
    }

    public MultiSelectSearchableFilterField(String name, List<SelectOption> options) {
        super(name, options);
    }

    @Override
    public FilterFieldType getType() {
        return FilterFieldType.MULTI_SELECT_SEARCHABLE_FILTER;
    }

}
