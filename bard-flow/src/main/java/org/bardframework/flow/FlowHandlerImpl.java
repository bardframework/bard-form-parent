package org.bardframework.flow;

import lombok.extern.slf4j.Slf4j;
import org.bardframework.flow.form.FlowFormTemplate;
import org.bardframework.flow.repository.FlowDataRepository;

import java.util.List;

@Slf4j
public class FlowHandlerImpl extends FlowHandlerAbstract<FlowData> {

    public FlowHandlerImpl(FlowDataRepository<FlowData> flowDataRepository, List<FlowFormTemplate> forms) {
        super(flowDataRepository, forms);
    }
}
