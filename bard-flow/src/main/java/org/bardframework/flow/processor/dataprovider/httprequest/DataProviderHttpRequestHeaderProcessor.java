package org.bardframework.flow.processor.dataprovider.httprequest;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.collections4.IteratorUtils;

import java.util.List;
import java.util.Map;

/**
 * map http request header name to fieldName
 */
public class DataProviderHttpRequestHeaderProcessor extends DataProviderHttpRequestProcessorAbstract {

    public DataProviderHttpRequestHeaderProcessor(Map<String, String> mapper) {
        super(mapper);
    }

    @Override
    protected List<String> getValues(String name, HttpServletRequest httpRequest) {
        return IteratorUtils.toList(httpRequest.getHeaders(name).asIterator());
    }
}
