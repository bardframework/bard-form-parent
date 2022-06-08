package org.bardframework.form.field.value;

import org.bardframework.form.field.InputField;

public interface FieldValueProvider<F extends InputField<T>, T> {

    T getValue(F field);
}
