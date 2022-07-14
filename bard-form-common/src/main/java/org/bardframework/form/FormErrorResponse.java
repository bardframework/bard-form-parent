package org.bardframework.form;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormErrorResponse {
    private Map<String, String> fieldErrors = new HashMap<>();
    private List<String> errors = new ArrayList<>();

    public FormErrorResponse() {
    }

    public FormErrorResponse(Map<String, String> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

    public FormErrorResponse(List<String> errors) {
        this.errors = errors;
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(Map<String, String> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
