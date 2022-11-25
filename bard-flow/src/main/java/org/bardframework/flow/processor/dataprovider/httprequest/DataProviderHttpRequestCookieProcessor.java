package org.bardframework.flow.processor.dataprovider.httprequest;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * map http request cookie name to fieldName
 */
public class DataProviderHttpRequestCookieProcessor extends DataProviderHttpRequestProcessorAbstract {

    public DataProviderHttpRequestCookieProcessor(Map<String, String> mapper) {
        super(mapper);
    }

    @Override
    protected List<String> getValues(String name, HttpServletRequest httpRequest) {
        if (ArrayUtils.isEmpty(httpRequest.getCookies())) {
            return null;
        }
        List<String> values = new ArrayList<>();
        for (Cookie cookie : httpRequest.getCookies()) {
            if (Objects.equals(cookie.getName(), name)) {
                values.add(cookie.getValue());
            }
        }
        return values;
    }
}
