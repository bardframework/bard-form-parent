package org.bardframework.flow.exception;

public class MaxOtpSendExceededException extends InvalidateFlowException {

    public MaxOtpSendExceededException(String flowToken, String message) {
        super(flowToken, message);
    }

    public MaxOtpSendExceededException(String flowToken, Throwable cause) {
        super(flowToken, cause);
    }

    public MaxOtpSendExceededException(String flowToken, String message, String messageKey) {
        super(flowToken, message, messageKey);
    }
}
