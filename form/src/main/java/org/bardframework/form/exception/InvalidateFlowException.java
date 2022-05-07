package org.bardframework.form.exception;

public class InvalidateFlowException extends Exception {
    private final String flowToken;

    public InvalidateFlowException(String flowToken, String message) {
        super(message);
        this.flowToken = flowToken;
    }

    public InvalidateFlowException(String flowToken, Throwable cause) {
        super(cause);
        this.flowToken = flowToken;
    }

    public String getFlowToken() {
        return flowToken;
    }
}
