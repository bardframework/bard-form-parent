package org.bardframework.form.flow;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class FlowHandlerChain extends FlowHandlerAbstract<FlowData> {
    private final List<FlowHandlerAbstract<FlowData>> flowHandlers;
    private final PreProcessorExecutionStrategy preProcessorExecutionStrategy;
    private final PostProcessorExecutionStrategy postProcessorExecutionStrategy;

    public FlowHandlerChain(List<FlowHandlerAbstract<FlowData>> flowHandlers) {
        this(flowHandlers, PreProcessorExecutionStrategy.EXECUTE_FIRST, PostProcessorExecutionStrategy.EXECUTE_LAST);
    }

    public FlowHandlerChain(List<FlowHandlerAbstract<FlowData>> flowHandlers, PreProcessorExecutionStrategy preProcessorExecutionStrategy, PostProcessorExecutionStrategy postProcessorExecutionStrategy) {
        super(flowHandlers.get(0).getFlowDataRepository(), flowHandlers.stream().map(handler -> handler.forms).flatMap(Collection::stream).collect(Collectors.toList()));
        this.flowHandlers = flowHandlers;
        this.preProcessorExecutionStrategy = preProcessorExecutionStrategy;
        this.postProcessorExecutionStrategy = postProcessorExecutionStrategy;
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

    public PreProcessorExecutionStrategy getPreProcessorExecutionStrategy() {
        return preProcessorExecutionStrategy;
    }

    public PostProcessorExecutionStrategy getPostProcessorExecutionStrategy() {
        return postProcessorExecutionStrategy;
    }

    public List<FlowHandlerAbstract<FlowData>> getFlowHandlers() {
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
