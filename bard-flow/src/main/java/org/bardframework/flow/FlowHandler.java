package org.bardframework.flow;

import org.apache.commons.lang3.RandomStringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.Map;

public interface FlowHandler {
    String TOKEN_HEADER_NAME = "X-Flow-Token";

    FlowResponse start(Map<String, String> initValues, Locale locale, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
            throws Exception;

    FlowResponse submit(String flowToken, Map<String, String> formData, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
            throws Exception;

    void action(String flowToken, String action, Map<String, String> formData, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
            throws Exception;

    FlowResponse getCurrent(String flowToken, Locale locale, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
            throws Exception;

    default String generateFlowToken() {
        return RandomStringUtils.randomAlphanumeric(10, 50);
    }
}
