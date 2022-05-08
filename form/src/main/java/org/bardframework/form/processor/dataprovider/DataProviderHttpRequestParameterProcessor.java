package org.bardframework.form.processor.dataprovider;

import org.bardframework.form.processor.FormProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.Map;

/**
 * map: http request parameter name -> fieldName
 * join values with ',' separator if more than one value exist for a parameter.
 */
public class DataProviderHttpRequestParameterProcessor implements FormProcessor {

    protected static final Logger LOGGER = LoggerFactory.getLogger(DataProviderHttpRequestParameterProcessor.class);
    protected final Map<String, String> requestParameterMapper;

    public DataProviderHttpRequestParameterProcessor(Map<String, String> requestParameterMapper) {
        this.requestParameterMapper = requestParameterMapper;
    }

    @Override
    public void process(String flowToken, Map<String, String> flowData, Map<String, String> formData, Locale locale, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
        for (Map.Entry<String, String> entry : this.getRequestParameterMapper().entrySet()) {
            String parameterName = entry.getKey();
            String fieldName = entry.getValue();
            String[] values = httpRequest.getParameterValues(parameterName);
            flowData.put(fieldName, null == values || values.length == 0 ? null : String.join(",", values));
        }
    }

    public Map<String, String> getRequestParameterMapper() {
        return requestParameterMapper;
    }
}
