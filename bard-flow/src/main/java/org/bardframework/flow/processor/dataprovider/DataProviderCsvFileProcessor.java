package org.bardframework.flow.processor.dataprovider;

import org.apache.commons.lang3.StringUtils;
import org.bardframework.commons.web.utils.ResourceUtils;
import org.bardframework.commons.web.utils.WebUtils;
import org.bardframework.flow.exception.FlowExecutionException;
import org.bardframework.flow.processor.FormProcessorAbstract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DataProviderCsvFileProcessor extends FormProcessorAbstract {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataProviderCsvFileProcessor.class);

    protected final String csvFileLocation;
    protected final Map<Integer, String> mapper;
    protected final String identifierFieldName;
    protected final String errorMessageCode;
    protected String separator = ",";

    public DataProviderCsvFileProcessor(String csvFileLocation, String identifierFieldName, Map<Integer, String> mapper, String errorMessageCode) {
        this.csvFileLocation = csvFileLocation;
        this.identifierFieldName = identifierFieldName;
        this.errorMessageCode = errorMessageCode;
        this.mapper = mapper;
    }

    @Override
    public void process(String flowToken, Map<String, String> flowData, Map<String, String> formData, Locale locale, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
        String identifier = flowData.get(this.getIdentifierFieldName());
        if (StringUtils.isBlank(identifier)) {
            LOGGER.warn("identifier not exist for [{}], can't read data from csv file", flowData);
            throw new FlowExecutionException(List.of("execution_error"));
        }
        try (InputStream inputStream = ResourceUtils.getResource(this.getCsvFileLocation()).getInputStream()) {
            try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream)) {
                try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
                    String line;
                    do {
                        line = reader.readLine();
                        if (null == line) {
                            continue;
                        }
                        String[] parts = new String(line.getBytes(), StandardCharsets.UTF_8).split(this.getSeparator());
                        if (parts[0].trim().equalsIgnoreCase(identifier)) {
                            for (Integer index : mapper.keySet()) {
                                if (index >= parts.length) {
                                    LOGGER.warn("can't find attribute[{}] value for[{}]", mapper.get(index), identifier);
                                } else {
                                    flowData.put(mapper.get(index), WebUtils.escapeString(parts[index]));
                                }
                            }
                            return;
                        }
                    } while (line != null);
                }
            }
        }
        LOGGER.warn("can't find record for[{}] in csv file [{}]", identifier, this.getCsvFileLocation());
        throw new FlowExecutionException(List.of(errorMessageCode));
    }

    public String getCsvFileLocation() {
        return csvFileLocation;
    }

    public Map<Integer, String> getMapper() {
        return mapper;
    }

    public String getIdentifierFieldName() {
        return identifierFieldName;
    }

    public String getErrorMessageCode() {
        return errorMessageCode;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }
}
