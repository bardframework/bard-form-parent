package org.bardframework.form.processor.dataprovider.httprequest;

import org.bardframework.commons.utils.ReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * mapL: http request property to fieldName
 */
public class DataProviderHttpRequestPropertyProcessor extends DataProviderHttpRequestProcessorAbstract {

    public DataProviderHttpRequestPropertyProcessor(Map<String, String> mapper) {
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
