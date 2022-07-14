package org.bardframework.form.processor;

import org.bardframework.form.flow.FlowData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.SpelCompilerMode;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public abstract class FormProcessorAbstract implements FormProcessor {

    protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private Expression executeExpression = null;

    public void setExecuteExpression(String executeExpression) {
        this.executeExpression = new SpelExpressionParser(new SpelParserConfiguration(SpelCompilerMode.IMMEDIATE, null)).parseExpression(executeExpression);
    }

    @Override
    public boolean mustExecute(FlowData flowData) {
        return null == executeExpression || Boolean.TRUE.equals(executeExpression.getValue(new StandardEvaluationContext(flowData), Boolean.class));
    }
}
