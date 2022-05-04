package org.bardframework.form.template;

import org.bardframework.form.model.SelectOptionsDataProvider;
import org.bardframework.form.processor.FormProcessor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormFieldTemplate {
    private final String name;
    private final String type;
    private final boolean volatile_;
    private SelectOptionsDataProvider optionsDataProvider;
    private List<FormProcessor> preProcessors;
    private List<FormProcessor> postProcessors;
    private Map<String, List<FormProcessor>> actionProcessors = new HashMap<>();

    public FormFieldTemplate(String name, String type) {
        this(name, type, false);
    }

    public FormFieldTemplate(String name, String type, boolean volatile_) {
        this.name = name;
        this.type = type;
        this.volatile_ = volatile_;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public SelectOptionsDataProvider getOptionsDataProvider() {
        return optionsDataProvider;
    }

    public void setOptionsDataProvider(SelectOptionsDataProvider optionsDataProvider) {
        this.optionsDataProvider = optionsDataProvider;
    }

    public boolean isVolatile() {
        return volatile_;
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

    public Map<String, List<FormProcessor>> getActionProcessors() {
        return actionProcessors;
    }

    public void setActionProcessors(Map<String, List<FormProcessor>> actionProcessors) {
        this.actionProcessors = actionProcessors;
    }
}
