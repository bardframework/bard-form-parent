package org.bardframework.flow;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public interface FlowController {

    @GetMapping(value = "", produces = APPLICATION_JSON_VALUE)
    default FlowResponse<String> start(Locale locale, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
            throws Exception {
        return this.getFlowHandler().start(locale, httpRequest, httpResponse);
    }

    @PostMapping(value = "", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    default FlowResponse<String> submit(@RequestHeader("X-Flow-Token") String flowToken, @RequestBody Map<String, String> formData, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
            throws Exception {
        return this.getFlowHandler().submit(flowToken, formData, httpRequest, httpResponse);
    }

    @PutMapping(value = "", consumes = APPLICATION_JSON_VALUE)
    default void action(@RequestHeader("X-Flow-Token") String flowToken, @RequestParam("action") String action, @RequestBody Map<String, String> formData, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
            throws Exception {
        this.getFlowHandler().action(flowToken, action, formData, httpRequest, httpResponse);
    }

    FlowHandler getFlowHandler();
}
