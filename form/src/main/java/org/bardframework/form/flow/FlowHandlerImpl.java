package org.bardframework.form.flow;

import org.bardframework.form.FormTemplate;
import org.bardframework.form.flow.repository.FlowDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class FlowHandlerImpl extends FlowHandlerAbstract<FlowData> {

    protected static final Logger LOGGER = LoggerFactory.getLogger(FlowHandlerImpl.class);

    public FlowHandlerImpl(FlowDataRepository<FlowData> flowDataRepository, List<FormTemplate> forms) {
        super(flowDataRepository, forms);
    }
}
