package org.bardframework.form.processor.dataprovider;

import org.bardframework.commons.utils.ReflectionUtils;
import org.bardframework.form.processor.FormProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.Map;

/**
 * mapL: http request property -> fieldName
 */
public class DataProviderHttpRequestPropertyProcessor implements FormProcessor {

    protected static final Logger LOGGER = LoggerFactory.getLogger(DataProviderHttpRequestPropertyProcessor.class);
    protected final Map<String, String> requestPropertyMapper;

    public DataProviderHttpRequestPropertyProcessor(Map<String, String> requestPropertyMapper) {
        this.requestPropertyMapper = requestPropertyMapper;
    }

    @Override
    public void process(String flowToken, Map<String, String> flowData, Map<String, String> formData, Locale locale, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
        for (Map.Entry<String, String> entry : this.getRequestPropertyMapper().entrySet()) {
            String propertyName = entry.getKey();
            String fieldName = entry.getValue();
            Object value = ReflectionUtils.getPropertyValue(httpRequest, propertyName);
            flowData.put(fieldName, null == value ? null : value.toString());
        }
    }

    public Map<String, String> getRequestPropertyMapper() {
        return requestPropertyMapper;
    }
}
