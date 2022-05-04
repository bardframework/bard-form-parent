package org.bardframework.form.flow.exception;

import java.util.HashMap;
import java.util.Map;

public class FlowDataValidationException extends RuntimeException {
    private final Map<String, String> fieldErrors = new HashMap<>();

    public FlowDataValidationException() {
        super("invalid field value");
    }

    public FlowDataValidationException addFiledError(String field, String errorMessage) {
        this.fieldErrors.put(field, errorMessage);
        return this;
    }

    public FlowDataValidationException addFiledError(String field) {
        this.fieldErrors.put(field, null);
        return this;
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }
}