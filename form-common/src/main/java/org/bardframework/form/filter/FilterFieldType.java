package org.bardframework.form.filter;

import org.bardframework.form.common.FieldType;

public enum FilterFieldType implements FieldType<FilterFieldType> {
    DATE_FILTER,
    DATE_TIME_FILTER,
    MULTI_SELECT_FILTER,
    MULTI_SELECT_SEARCHABLE_FILTER,
    NUMBER_FILTER,
    SINGLE_SELECT_FILTER,
    SINGLE_SELECT_SEARCHABLE_FILTER,
    TEXT_FILTER,
    TIME_FILTER
}
