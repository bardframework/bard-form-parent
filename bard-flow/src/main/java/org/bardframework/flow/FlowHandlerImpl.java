package org.bardframework.flow;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.bardframework.flow.form.FlowFormTemplate;
import org.bardframework.flow.repository.FlowDataRepository;

import java.util.List;
import java.util.Map;

public class FlowHandlerImpl extends FlowHandlerAbstract<FlowData> {

    public FlowHandlerImpl(String name, FlowDataRepository<FlowData> flowDataRepository, List<FlowFormTemplate> forms) {
        super(name, flowDataRepository, forms);
    }

    @Override
    protected FlowResponse handleException(String flowToken, FlowData flowData, Map<String, Object> formData, FlowFormTemplate currentFormTemplate, FlowAction flowAction, HttpServletRequest httpRequest, HttpServletResponse httpResponse, Exception ex) throws Exception {
        throw ex;
    }
}
