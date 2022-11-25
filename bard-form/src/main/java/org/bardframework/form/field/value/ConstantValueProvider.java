package org.bardframework.form.field.value;

import jakarta.servlet.http.HttpServletRequest;
import org.bardframework.form.field.input.InputField;

public abstract class ConstantValueProvider<T, F extends InputField<T>> implements FieldValueProvider<F, T> {

    private final T value;

    public ConstantValueProvider(T value) {
        this.value = value;
    }

    @Override
    public T getValue(F field, HttpServletRequest httpRequest) {
        return value;
    }
}
