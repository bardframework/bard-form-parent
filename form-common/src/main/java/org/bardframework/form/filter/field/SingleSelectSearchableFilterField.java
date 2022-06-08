package org.bardframework.form.filter.field;

import org.bardframework.form.common.SelectOption;
import org.bardframework.form.filter.FilterFieldType;

import java.util.List;

public class SingleSelectSearchableFilterField extends SingleSelectFilterField {

    public SingleSelectSearchableFilterField() {
    }

    public SingleSelectSearchableFilterField(String name, List<SelectOption> options) {
        super(name, options);
    }

    @Override
    public FilterFieldType getType() {
        return FilterFieldType.SINGLE_SELECT_SEARCHABLE_FILTER;
    }

}
