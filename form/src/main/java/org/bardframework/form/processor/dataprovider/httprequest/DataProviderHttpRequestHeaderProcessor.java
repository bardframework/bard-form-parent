package org.bardframework.form.processor.dataprovider.httprequest;

import org.apache.commons.collections4.IteratorUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * map: http request header name -> fieldName
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
