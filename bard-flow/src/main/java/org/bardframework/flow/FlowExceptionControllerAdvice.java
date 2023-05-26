package org.bardframework.flow;

import org.apache.commons.lang3.StringUtils;
import org.bardframework.flow.exception.FlowDataValidationException;
import org.bardframework.flow.exception.FlowExecutionException;
import org.bardframework.form.exception.FormDataValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface FlowExceptionControllerAdvice {

    Logger log = LoggerFactory.getLogger(FlowExceptionControllerAdvice.class);

    @ExceptionHandler(FlowDataValidationException.class)
    default ResponseEntity<FlowResponse> handle(FlowDataValidationException ex) {
        log.debug("flow data validation error.", ex);
        final Map<String, String> errors = new HashMap<>();
        for (String errorFieldName : ex.getFieldErrors().keySet()) {
            String errorMessageCode = ex.getFieldErrors().get(errorFieldName);
            if (StringUtils.isNotBlank(errorMessageCode)) {
                errors.put(errorFieldName, this.getMessageSource().getMessage(errorMessageCode, null, errorMessageCode, LocaleContextHolder.getLocale()));
            } else {
                errors.put(errorFieldName, "");
            }
        }
        FlowResponse response = new FlowResponse();
        response.setFieldErrors(errors);
        response.setForm(ex.getForm());
        return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(FlowExecutionException.class)
    default ResponseEntity<FlowResponse> handle(FlowExecutionException ex) {
        log.debug("flow execution error: {}", ex.getErrorsMessageCodes());
        List<String> errors = ex.getErrorsMessageCodes()
                .stream()
                .map(errorMessageCode -> this.getMessageSource().getMessage(errorMessageCode, null, errorMessageCode, LocaleContextHolder.getLocale()))
                .collect(Collectors.toList());
        FlowResponse response = new FlowResponse();
        response.setErrors(errors);
        return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(FormDataValidationException.class)
    default ResponseEntity<FlowResponse> handle(FormDataValidationException ex) {
        log.debug("form data validation error: {}", ex.getMessage());
        Map<String, String> fieldErrors = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : ex.getInvalidFields().entrySet()) {
            fieldErrors.put(entry.getKey(), String.join("\n", entry.getValue()));
        }

        FlowResponse response = new FlowResponse();
        response.setFieldErrors(fieldErrors);
        return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
    }

    MessageSource getMessageSource();
}
