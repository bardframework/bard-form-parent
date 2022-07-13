package org.bardframework.form.flow;

import org.bardframework.form.FormTemplate;
import org.bardframework.form.flow.repository.FlowDataRepository;
import org.bardframework.form.processor.FormProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlowHandlerImpl extends FlowHandlerAbstract {

    protected static final Logger LOGGER = LoggerFactory.getLogger(FlowHandlerImpl.class);
    protected final List<FormTemplate> forms;
    private List<FormProcessor> preProcessors = new ArrayList<>();
    private List<FormProcessor> postProcessors = new ArrayList<>();
    private Map<String, List<FormProcessor>> actionProcessors = new HashMap<>();

    public FlowHandlerImpl(FlowDataRepository<FlowData> flowDataRepository, List<FormTemplate> forms) {
        super(flowDataRepository);
        this.forms = forms;
    }

    public List<FormTemplate> getForms() {
        return forms;
    }

    protected List<FormProcessor> getPreProcessors() {
        return preProcessors;
    }

    public void setPreProcessors(List<FormProcessor> preProcessors) {
        this.preProcessors = preProcessors;
    }

    public List<FormProcessor> getPostProcessors() {
        return postProcessors;
    }

    public void setPostProcessors(List<FormProcessor> postProcessors) {
        this.postProcessors = postProcessors;
    }

    public Map<String, List<FormProcessor>> getActionProcessors() {
        return actionProcessors;
    }

    public void setActionProcessors(Map<String, List<FormProcessor>> actionProcessors) {
        this.actionProcessors = actionProcessors;
    }
}
