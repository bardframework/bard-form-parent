package org.bardframework.flow;

import org.bardframework.flow.repository.FlowDataRepository;
import org.bardframework.form.FormTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class FlowHandlerImpl extends FlowHandlerAbstract<FlowData> {

    protected static final Logger LOGGER = LoggerFactory.getLogger(FlowHandlerImpl.class);

    public FlowHandlerImpl(FlowDataRepository<FlowData> flowDataRepository, List<FormTemplate> forms) {
        super(flowDataRepository, forms);
    }
}
