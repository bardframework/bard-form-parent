package org.bardframework.flow.processor.dataprovider.httprequest;

import jakarta.servlet.http.HttpServletRequest;
import org.bardframework.commons.utils.ReflectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * map http request property to fieldName
 */
public class DataProviderHttpRequestPropertyProcessor extends DataProviderHttpRequestProcessorAbstract {

    public DataProviderHttpRequestPropertyProcessor(Map<String, Object> mapper) {
        super(mapper);
    }

    @Override
    protected List<String> getValues(String name, HttpServletRequest httpRequest) throws Exception {
        Object value = ReflectionUtils.getPropertyValue(httpRequest, name);
        if (null == value) {
            return Collections.emptyList();
        }
        return List.of(value.toString());
    }
}
