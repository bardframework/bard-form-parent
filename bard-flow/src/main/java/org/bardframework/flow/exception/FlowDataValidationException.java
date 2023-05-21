package org.bardframework.flow.exception;

import lombok.Getter;
import lombok.Setter;
import org.bardframework.form.BardForm;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class FlowDataValidationException extends RuntimeException {

    private final boolean sendCurrentForm;
    private BardForm form;
    private final Map<String, String> fieldErrors = new HashMap<>();

    public FlowDataValidationException() {
        this(false);
    }

    public FlowDataValidationException(boolean sendCurrentForm) {
        super("invalid field value");
        this.sendCurrentForm = sendCurrentForm;
    }

    public FlowDataValidationException addFieldError(String field, String errorMessage) {
        this.fieldErrors.put(field, errorMessage);
        return this;
    }
}