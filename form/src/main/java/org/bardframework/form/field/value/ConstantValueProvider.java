package org.bardframework.form.field.value;

import org.bardframework.form.common.FormField;

public abstract class ConstantValueProvider<T, F extends FormField<T>> implements FieldValueProvider<F, T> {

    private final T value;

    public ConstantValueProvider(T value) {
        this.value = value;
    }

    @Override
    public T getValue(F field) {
        return value;
    }
}
