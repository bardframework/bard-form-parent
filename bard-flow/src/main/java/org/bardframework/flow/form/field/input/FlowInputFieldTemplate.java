package org.bardframework.flow.form.field.input;

import org.bardframework.flow.form.FormProcessor;
import org.bardframework.form.field.input.InputField;
import org.bardframework.form.field.input.InputFieldTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class FlowInputFieldTemplate<F extends InputField<T>, T> extends InputFieldTemplate<F, T> {

    protected List<FormProcessor> preProcessors;
    protected List<FormProcessor> postProcessors;
    protected Map<String, List<FormProcessor>> actionProcessors = new HashMap<>();

    protected FlowInputFieldTemplate(String name) {
        super(name);
    }

    public FlowInputFieldTemplate(String name, boolean persistentValue) {
        super(name, persistentValue);
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
