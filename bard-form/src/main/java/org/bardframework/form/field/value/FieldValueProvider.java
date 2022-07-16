package org.bardframework.form.field.value;

import org.bardframework.form.field.input.InputField;

import javax.servlet.http.HttpServletRequest;

public interface FieldValueProvider<F extends InputField<T>, T> {

    T getValue(F field, HttpServletRequest httpRequest);
}
