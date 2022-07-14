package org.bardframework.flow.form;

import org.apache.commons.collections4.CollectionUtils;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.field.FieldTemplate;
import org.bardframework.form.field.input.InputFieldTemplate;
import org.bardframework.form.processor.FormProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlowFormTemplate extends FormTemplate {

    protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private List<FormProcessor> preProcessors;
    private List<FormProcessor> postProcessors;
    private Map<String, List<FormProcessor>> actionProcessors = new HashMap<>();
    private boolean finished;

    public FlowFormTemplate(String name, List<FieldTemplate<?>> fieldTemplates, MessageSource messageSource) {
        super(name, fieldTemplates, messageSource);
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
        List<InputFieldTemplate<?, ?>> inputFieldTemplates = new ArrayList<>();
        fieldTemplates.stream().filter(fieldTemplate -> fieldTemplate instanceof InputFieldTemplate).forEach(fieldTemplate -> {
            inputFieldTemplates.add((InputFieldTemplate<?, ?>) fieldTemplate);
        });
        /*
            merge all pre processors
         */
        List<FormProcessor> processors = new ArrayList<>();
        for (InputFieldTemplate<?, ?> inputFieldTemplate : inputFieldTemplates) {
            if (null != inputFieldTemplate.getPreProcessors()) {
                processors.addAll(inputFieldTemplate.getPreProcessors());
            }
        }
        if (null != this.preProcessors) {
            processors.addAll(this.preProcessors);
        }
        processors.sort(FormProcessor::compareTo);
        this.preProcessors = processors;

        /*
            merge all post processors
         */
        processors = new ArrayList<>();
        for (InputFieldTemplate<?, ?> inputFieldTemplate : inputFieldTemplates) {
            if (null != inputFieldTemplate.getPostProcessors()) {
                processors.addAll(inputFieldTemplate.getPostProcessors());
            }
        }
        if (null != this.postProcessors) {
            processors.addAll(this.postProcessors);
        }
        processors.sort(FormProcessor::compareTo);
        this.postProcessors = processors;
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