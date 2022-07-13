package org.bardframework.form.flow;

import org.bardframework.form.FormTemplate;
import org.bardframework.form.processor.FormProcessor;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

public class FlowHandlerChain extends FlowHandlerAbstract {
    private final List<FlowHandlerAbstract> flowHandlers;
    private final List<FormTemplate> forms;
    private final PreProcessorExecutionStrategy preProcessorExecutionStrategy;
    private final PostProcessorExecutionStrategy postProcessorExecutionStrategy;
    private List<FormProcessor> preProcessors = new ArrayList<>();
    private List<FormProcessor> postProcessors = new ArrayList<>();
    private Map<String, List<FormProcessor>> actionProcessors = new HashMap<>();

    public FlowHandlerChain(List<FlowHandlerAbstract> flowHandlers) {
        this(flowHandlers, PreProcessorExecutionStrategy.EXECUTE_FIRST, PostProcessorExecutionStrategy.EXECUTE_LAST);
    }

    public FlowHandlerChain(List<FlowHandlerAbstract> flowHandlers, PreProcessorExecutionStrategy preProcessorExecutionStrategy, PostProcessorExecutionStrategy postProcessorExecutionStrategy) {
        super(flowHandlers.get(0).getFlowDataRepository());
        this.flowHandlers = flowHandlers;
        this.preProcessorExecutionStrategy = preProcessorExecutionStrategy;
        this.postProcessorExecutionStrategy = postProcessorExecutionStrategy;
        this.forms = flowHandlers.stream().map(FlowHandlerAbstract::getForms).flatMap(Collection::stream).collect(Collectors.toList());
        this.actionProcessors = new HashMap<>();
        this.getFlowHandlers().stream().map(FlowHandlerAbstract::getActionProcessors).forEach(this.actionProcessors::putAll);
    }

    @PostConstruct
    void init() {
        if (this.getPreProcessorExecutionStrategy() == PreProcessorExecutionStrategy.EXECUTE_FIRST) {
            this.preProcessors.addAll(this.getFlowHandlers().get(0).getPreProcessors());
        } else if (this.getPreProcessorExecutionStrategy() == PreProcessorExecutionStrategy.EXECUTE_ALL) {
            this.preProcessors.addAll(this.getFlowHandlers().stream().map(FlowHandlerAbstract::getPreProcessors).flatMap(Collection::stream).collect(Collectors.toList()));
        }
        if (this.getPostProcessorExecutionStrategy() == PostProcessorExecutionStrategy.EXECUTE_LAST) {
            this.postProcessors.addAll(0, this.getFlowHandlers().get(this.getFlowHandlers().size() - 1).getPostProcessors());
        } else if (this.getPostProcessorExecutionStrategy() == PostProcessorExecutionStrategy.EXECUTE_ALL) {
            this.postProcessors.addAll(0, this.getFlowHandlers().stream().map(FlowHandlerAbstract::getPostProcessors).flatMap(Collection::stream).collect(Collectors.toList()));
        }
    }

    @Override
    protected List<FormTemplate> getForms() {
        return forms;
    }

    @Override
    protected List<FormProcessor> getPreProcessors() {
        return preProcessors;
    }

    public void setPreProcessors(List<FormProcessor> preProcessors) {
        this.preProcessors = preProcessors;
    }

    @Override
    protected List<FormProcessor> getPostProcessors() {
        return postProcessors;
    }

    public void setPostProcessors(List<FormProcessor> postProcessors) {
        this.postProcessors = postProcessors;
    }

    @Override
    protected Map<String, List<FormProcessor>> getActionProcessors() {
        return actionProcessors;
    }

    public void setActionProcessors(Map<String, List<FormProcessor>> actionProcessors) {
        this.actionProcessors = actionProcessors;
    }

    public PreProcessorExecutionStrategy getPreProcessorExecutionStrategy() {
        return preProcessorExecutionStrategy;
    }

    public PostProcessorExecutionStrategy getPostProcessorExecutionStrategy() {
        return postProcessorExecutionStrategy;
    }

    public List<FlowHandlerAbstract> getFlowHandlers() {
        return flowHandlers;
    }

    protected enum PreProcessorExecutionStrategy {
        /*
            هیچ کدام اجرا نخواهد شد
         */
        EXECUTE_NONE,
        /*
            فقط پیش پردازش اولین فلو اجرا خواهد شد
         */
        EXECUTE_FIRST,
        /*
            پیش پردازش های همه ی فلوها ادغام شده و در ابتدای فلو اجرا خواهد شد
         */
        EXECUTE_ALL
    }

    protected enum PostProcessorExecutionStrategy {
        /*
            هیچ کدام اجرا نخواهد شد
         */
        EXECUTE_NONE,
        /*
            فقط پس پردازش آخرین فلو اجرا خواهد شد
         */
        EXECUTE_LAST,
        /*
            پس پردازش های همه ی فلوها ادغام شده و در انتهای فلو اجرا خواهد شد
         */
        EXECUTE_ALL
    }
}
