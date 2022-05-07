package org.bardframework.form.field.value;

import org.bardframework.form.common.field.base.FormField;

public interface FieldValueProvider<F extends FormField<T>, T> {

    T getValue(F field);
}
