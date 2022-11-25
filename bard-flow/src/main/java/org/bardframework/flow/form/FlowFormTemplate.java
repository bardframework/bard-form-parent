package org.bardframework.flow.form;

import jakarta.annotation.PostConstruct;
import org.apache.commons.collections4.CollectionUtils;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.field.FieldTemplate;
import org.springframework.context.MessageSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlowFormTemplate extends FormTemplate {

    private List<FormProcessor> preProcessors = new ArrayList<>();
    private List<FormProcessor> postProcessors = new ArrayList<>();
    private Map<String, List<FormProcessor>> actionProcessors = new HashMap<>();
    private boolean finished;

    public FlowFormTemplate(String name, List<FieldTemplate<?>> fieldTemplates, MessageSource messageSource) {
        super(name, fieldTemplates, messageSource);
    }

    @PostConstruct
    protected void configurationValidate() {
        if (CollectionUtils.isNotEmpty(this.getPostProcessors()) && this.isFinished()) {
            throw new IllegalStateException("when finished is true, can't set postProcessors");
        }
        this.getPreProcessors().forEach(processor -> processor.configurationValidate(this));
        this.getPostProcessors().forEach(processor -> processor.configurationValidate(this));
    }

    public List<FormProcessor> getPreProcessors() {
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

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public Map<String, List<FormProcessor>> getActionProcessors() {
        return actionProcessors;
    }

    public void setActionProcessors(Map<String, List<FormProcessor>> actionProcessors) {
        this.actionProcessors = actionProcessors;
    }
}