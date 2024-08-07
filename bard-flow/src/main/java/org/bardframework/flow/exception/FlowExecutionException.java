package org.bardframework.flow.exception;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
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

}