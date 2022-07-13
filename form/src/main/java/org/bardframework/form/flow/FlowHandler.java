package org.bardframework.form.flow;

import org.apache.commons.lang3.RandomStringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.Map;

public interface FlowHandler {

    default FlowResponse<String> start(Locale locale, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
            throws Exception {
        return this.start(this.generateFlowToken(), locale, httpRequest, httpResponse);
    }

    FlowResponse<String> start(String flowToken, Locale locale, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
            throws Exception;

    FlowResponse<String> submit(String flowToken, Map<String, String> formData, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
            throws Exception;

    void action(String flowToken, String action, Map<String, String> formData, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
            throws Exception;

    FlowResponse<String> getCurrent(String flowToken, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
            throws Exception;

    default String generateFlowToken() {
        return RandomStringUtils.randomAlphanumeric(10, 50);
    }
}
