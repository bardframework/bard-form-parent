package org.bardframework.flow.exception;

import lombok.Getter;

@Getter
public class InvalidateFlowException extends Exception {
    private final String flowToken;
    private String messageKey;

    public InvalidateFlowException(String flowToken, String message) {
        super(message);
        this.flowToken = flowToken;
    }

    public InvalidateFlowException(String flowToken, Throwable cause) {
        super(cause);
        this.flowToken = flowToken;
    }

    public InvalidateFlowException(String flowToken, String message, String messageKey) {
        super(message);
        this.flowToken = flowToken;
        this.messageKey = messageKey;
    }
}
