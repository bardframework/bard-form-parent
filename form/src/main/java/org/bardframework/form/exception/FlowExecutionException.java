package org.bardframework.form.exception;

import java.util.ArrayList;
import java.util.List;

public class FlowExecutionException extends RuntimeException {
    private final List<String> errorsMessageCodes = new ArrayList<>();

    public FlowExecutionException(List<String> errorsMessageCodes) {
        super("flow execution error");
        this.errorsMessageCodes.addAll(errorsMessageCodes);
    }

    public FlowExecutionException addErrorMessageCode(String errorMessageCode) {
        this.errorsMessageCodes.add(errorMessageCode);
        return this;
    }

    public List<String> getErrorsMessageCodes() {
        return errorsMessageCodes;
    }
}