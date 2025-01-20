package org.bardframework.flow;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Locale;
import java.util.Map;

public interface FlowHandler {
    String TOKEN_HEADER_NAME = "X-Flow-Token";

    FlowResponse start(Map<String, Object> initValues, Locale locale, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
            throws Exception;

    FlowResponse submit(String flowToken, Map<String, Object> formData, Locale locale, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
            throws Exception;

    Object action(String flowToken, String action, Map<String, Object> formData, Locale locale, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
            throws Exception;

    FlowResponse getCurrent(String flowToken, Locale locale, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
            throws Exception;

    default String generateFlowToken(Map<String, Object> initValues) {
        return RandomStringUtils.randomAlphanumeric(10, 50);
    }
}
