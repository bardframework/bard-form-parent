package org.bardframework.form.field.base;

public interface WithValueFieldTemplate<T> {

    String getName();

    T toValue(String stringValue);
}
