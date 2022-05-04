package org.bardframework.form.processor;

import org.apache.commons.lang3.StringUtils;
import org.bardframework.commons.web.utils.HttpCallResult;
import org.bardframework.commons.web.utils.HttpCaller;
import org.bardframework.form.flow.exception.FlowExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataProviderHttpCallProcessor extends HttpCaller implements FormProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataProviderHttpCallProcessor.class);

    protected final Map<String, Pattern> fieldsFetcher = new HashMap<>();
    private final Pattern foundPattern;
    private final String errorMessageCode;

    public DataProviderHttpCallProcessor(String httpMethod, String urlTemplate, String successPattern, Map<String, String> fieldsFetcher, String errorMessageCode) {
        super(httpMethod, urlTemplate);
        this.foundPattern = Pattern.compile(successPattern);
        this.errorMessageCode = errorMessageCode;
        for (Map.Entry<String, String> entry : fieldsFetcher.entrySet()) {
            this.fieldsFetcher.put(entry.getKey(), Pattern.compile(entry.getValue()));
        }
    }

    @Override
    public void process(String flowToken, Map<String, String> flowData, Map<String, String> formData, Locale locale, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
        HttpCallResult result = super.call(flowData);
        String responseString = null == result.getBody() ? null : new String(result.getBody(), StandardCharsets.UTF_8);
        LOGGER.debug("data provider http call result: [{}]", responseString);
        if (StringUtils.isBlank(responseString) || !this.getFoundPattern().matcher(responseString).find()) {
            LOGGER.warn("data not found calling ws[{}], [{}].", this.getUrlTemplate(), flowData);
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


    public Pattern getFoundPattern() {
        return foundPattern;
    }

    public Map<String, Pattern> getFieldsFetcher() {
        return fieldsFetcher;
    }

    public String getErrorMessageCode() {
        return errorMessageCode;
    }
}
