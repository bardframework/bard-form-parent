package org.bardframework.form.exception;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormDataValidationException extends RuntimeException {
    private final Map<String, List<String>> invalidFields = new HashMap<>();

    public FormDataValidationException() {
        super("invalid field value");
    }

    public FormDataValidationException addFiledError(String field, String message) {
        this.invalidFields.putIfAbsent(field, new ArrayList<>());
        this.invalidFields.get(field).add(message);
        return this;
    }

    public Map<String, List<String>> getInvalidFields() {
        return invalidFields;
    }
}
