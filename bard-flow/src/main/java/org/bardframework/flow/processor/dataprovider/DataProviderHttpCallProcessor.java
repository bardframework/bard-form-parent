package org.bardframework.flow.processor.dataprovider;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bardframework.commons.utils.http.HttpCallResponse;
import org.bardframework.commons.utils.http.HttpCaller;
import org.bardframework.flow.exception.FlowExecutionException;
import org.bardframework.flow.form.FormProcessor;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.SpelCompilerMode;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Setter
@Getter
public class DataProviderHttpCallProcessor extends HttpCaller implements FormProcessor {

    protected final Map<String, Pattern> fieldsFetcher = new HashMap<>();
    private final Pattern fetchPattern;
    private final String errorMessageCode;
    protected String responseFieldName;
    private Expression executeExpression = null;

    public DataProviderHttpCallProcessor(String httpMethod, String urlTemplate, String successPattern, Map<String, String> fieldsFetcher, String errorMessageCode) {
        super(httpMethod, urlTemplate);
        this.fetchPattern = Pattern.compile(successPattern);
        this.errorMessageCode = errorMessageCode;
        for (Map.Entry<String, String> entry : fieldsFetcher.entrySet()) {
            this.fieldsFetcher.put(entry.getKey(), Pattern.compile(entry.getValue()));
        }
    }

    @Override
    public void process(String flowToken, Map<String, String> flowData, Map<String, String> formData, Locale locale, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
        HttpCallResponse result = super.call(flowData);
        String responseString = null == result.getBody() ? null : new String(result.getBody(), StandardCharsets.UTF_8);
        log.debug("data provider http call result: [{}]", responseString);
        if (StringUtils.isNotBlank(this.getResponseFieldName())) {
            flowData.put(this.getResponseFieldName(), responseString);
        }
        if (StringUtils.isBlank(responseString) || !this.getFetchPattern().matcher(responseString).find()) {
            log.warn("data not found calling ws[{}], [{}].", this.getUrlTemplate(), flowData);
            throw new FlowExecutionException(List.of(this.getErrorMessageCode()));
        }
        Matcher matcher;
        for (Map.Entry<String, Pattern> entry : this.getFieldsFetcher().entrySet()) {
            matcher = entry.getValue().matcher(responseString);
            if (matcher.find()) {
                flowData.put(entry.getKey(), matcher.group(1));
            }
        }
    }

    public void setExecuteExpression(String executeExpression) {
        this.executeExpression = new SpelExpressionParser(new SpelParserConfiguration(SpelCompilerMode.IMMEDIATE, null)).parseExpression(executeExpression);
    }

    @Override
    public boolean mustExecute(Map<String, String> args) {
        return null == executeExpression || Boolean.TRUE.equals(executeExpression.getValue(new StandardEvaluationContext(args), Boolean.class));
    }
}
