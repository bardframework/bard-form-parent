package org.bardframework.flow;

import org.bardframework.flow.form.FlowFormTemplate;
import org.bardframework.flow.repository.FlowDataRepository;

import java.util.List;
import java.util.Map;

public class FlowHandlerImpl extends FlowHandlerAbstract<FlowData> {

    public FlowHandlerImpl(FlowDataRepository<FlowData> flowDataRepository, List<FlowFormTemplate> forms) {
        super(flowDataRepository, forms);
    }

    @Override
    protected FlowResponse handleException(String flowToken, FlowData flowData, Map<String, String> formData, FlowFormTemplate currentFormTemplate, Exception ex) throws Exception {
        throw ex;
    }
}
