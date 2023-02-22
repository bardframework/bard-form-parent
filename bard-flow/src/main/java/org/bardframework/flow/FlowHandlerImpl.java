package org.bardframework.flow;

import org.bardframework.flow.form.FlowFormTemplate;
import org.bardframework.flow.repository.FlowDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class FlowHandlerImpl extends FlowHandlerAbstract<FlowData> {

    protected static final Logger log = LoggerFactory.getLogger(FlowHandlerImpl.class);

    public FlowHandlerImpl(FlowDataRepository<FlowData> flowDataRepository, List<FlowFormTemplate> forms) {
        super(flowDataRepository, forms);
    }
}
