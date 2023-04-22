package org.bardframework.form;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Getter
@Setter
@ToString
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
}
