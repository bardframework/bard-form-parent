package org.bardframework.form.template;

import org.apache.commons.collections4.CollectionUtils;
import org.bardframework.form.processor.FormProcessor;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormTemplate {

    private final String name;
    private final List<FormFieldTemplate> fields;
    private List<FormProcessor> preProcessors;
    private List<FormProcessor> postProcessors;
    private Map<String, List<FormProcessor>> actionProcessors = new HashMap<>();
    private boolean finished;

    public FormTemplate(String name, List<FormFieldTemplate> fields) {
        this.name = name;
        this.fields = fields;
    }

    @PostConstruct
    protected void configurationValidate() {
        if (CollectionUtils.isNotEmpty(postProcessors)) {
            if (finished) {
                throw new IllegalStateException("when finished is true, can't set postProcessors");
            }
            this.postProcessors.forEach(processor -> processor.configurationValidate(this));
        }
        if (CollectionUtils.isNotEmpty(preProcessors)) {
            this.preProcessors.forEach(processor -> processor.configurationValidate(this));
        }
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

    public String getName() {
        return name;
    }

    public List<FormFieldTemplate> getFields() {
        return fields;
    }

    public Map<String, List<FormProcessor>> getActionProcessors() {
        return actionProcessors;
    }

    public void setActionProcessors(Map<String, List<FormProcessor>> actionProcessors) {
        this.actionProcessors = actionProcessors;
    }

    public FormFieldTemplate getField(String name) {
        return this.getFields().stream().filter(field -> field.getName().equals(name)).findFirst().orElse(null);
    }
}