package org.bardframework.form.common.field;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.bardframework.form.common.FormField;
import org.slf4j.LoggerFactory;

public interface FieldDataProvider<F extends FormField<T>, T> {

    T getValidValue(F field) throws Exception;

    default String getValidValueString(F field) throws Exception {
        T value = this.getValidValue(field);
        return field.toString(value);
    }

    T getInvalidValue(F field) throws Exception;

    default String getInvalidValueString(F field) throws Exception {
        T value = this.getInvalidValue(field);
        return field.toString(value);
    }

    void set(ObjectNode objectNode, String property, T value);

    boolean supports(FormField<?> field);

    default void setValidValue(ObjectNode objectNode, F field) throws Exception {
        this.set(objectNode, field.getName(), this.getValidValue(field));
    }

    default void setInvalidValue(ObjectNode objectNode, F field) throws Exception {
        T invalidValue = this.getInvalidValue(field);
        LoggerFactory.getLogger(this.getClass()).info("setting invalid value [{}] for field [{}]", invalidValue, field.getName());
        this.set(objectNode, field.getName(), invalidValue);
    }

}