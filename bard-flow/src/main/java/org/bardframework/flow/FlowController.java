package org.bardframework.flow;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.Map;

import static org.bardframework.flow.FlowHandler.TOKEN_HEADER_NAME;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public interface FlowController {
    String ACTION_PARAMETER_NAME = "action";

    @GetMapping(value = "", produces = APPLICATION_JSON_VALUE)
    default FlowResponse start(Locale locale, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
            throws Exception {
        return this.getFlowHandler().start(Map.of(), locale, httpRequest, httpResponse);
    }

    @GetMapping(value = "", produces = APPLICATION_JSON_VALUE, headers = TOKEN_HEADER_NAME)
    default FlowResponse getCurrent(@RequestHeader(TOKEN_HEADER_NAME) String flowToken, Locale locale, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
            throws Exception {
        return this.getFlowHandler().getCurrent(flowToken, locale, httpRequest, httpResponse);
    }

    @PostMapping(value = "", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    default FlowResponse submit(@RequestHeader(TOKEN_HEADER_NAME) String flowToken, @RequestBody Map<String, String> formData, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
            throws Exception {
        return this.getFlowHandler().submit(flowToken, formData, httpRequest, httpResponse);
    }

    @PutMapping(value = "", consumes = APPLICATION_JSON_VALUE)
    default void action(@RequestHeader(TOKEN_HEADER_NAME) String flowToken, @RequestParam(ACTION_PARAMETER_NAME) String action, @RequestBody Map<String, String> formData, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
            throws Exception {
        this.getFlowHandler().action(flowToken, action, formData, httpRequest, httpResponse);
    }

    FlowHandler getFlowHandler();
}
