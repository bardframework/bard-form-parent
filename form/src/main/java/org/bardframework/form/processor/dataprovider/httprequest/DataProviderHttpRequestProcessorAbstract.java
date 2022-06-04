package org.bardframework.form.processor.dataprovider.httprequest;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bardframework.form.processor.FormProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class DataProviderHttpRequestProcessorAbstract implements FormProcessor {

    protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    protected final Map<String, String> mapper;

    public DataProviderHttpRequestProcessorAbstract(Map<String, String> mapper) {
        this.mapper = mapper;
    }

    @Override
    public void process(String flowToken, Map<String, String> flowData, Map<String, String> formData, Locale locale, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
        for (Map.Entry<String, String> entry : this.getMapper().entrySet()) {
            String name = entry.getKey();
            String fieldName = entry.getValue();
            List<String> values = this.getValues(name, httpRequest);
            values = values.stream().filter(StringUtils::isNotBlank).collect(Collectors.toList());
            flowData.put(fieldName, CollectionUtils.isEmpty(values) ? null : String.join(this.getValuesSeparator(), values));
        }
    }

    public Map<String, String> getMapper() {
        return mapper;
    }

    public String getValuesSeparator() {
        return ",";
    }

    protected abstract List<String> getValues(String name, HttpServletRequest httpRequest) throws Exception;
}
