package org.bardframework.form.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.Map;

/**
 * map: http request header name -> fieldName
 */
public class DataProviderHttpRequestHeaderProcessor implements FormProcessor {

    protected static final Logger LOGGER = LoggerFactory.getLogger(DataProviderHttpRequestHeaderProcessor.class);
    protected final Map<String, String> requestHeaderMapper;

    public DataProviderHttpRequestHeaderProcessor(Map<String, String> requestHeaderMapper) {
        this.requestHeaderMapper = requestHeaderMapper;
    }

    @Override
    public void process(String flowToken, Map<String, String> flowData, Map<String, String> formData, Locale locale, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
        for (Map.Entry<String, String> entry : this.getRequestHeaderMapper().entrySet()) {
            String headerName = entry.getKey();
            String fieldName = entry.getValue();
            flowData.put(fieldName, httpRequest.getHeader(headerName));
        }
    }

    public Map<String, String> getRequestHeaderMapper() {
        return requestHeaderMapper;
    }
}
