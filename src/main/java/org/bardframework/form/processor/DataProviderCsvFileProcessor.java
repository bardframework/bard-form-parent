package org.bardframework.form.processor;

import org.apache.commons.lang3.StringUtils;
import org.bardframework.commons.web.utils.ResourceUtils;
import org.bardframework.commons.web.utils.WebUtils;
import org.bardframework.form.flow.exception.FlowExecutionException;
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

public class DataProviderCsvFileProcessor implements FormProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataProviderCsvFileProcessor.class);

    protected final String csvFileLocation;
    protected final int identifierIndexInCsvFile;
    protected final Map<Integer, String> fieldsMapper;
    protected final String identifierFieldName;
    protected final String errorMessageCode;

    public DataProviderCsvFileProcessor(String csvFileLocation, int identifierIndexInCsvFile, String identifierFieldName, Map<Integer, String> fieldsMapper, String errorMessageCode) {
        this.csvFileLocation = csvFileLocation;
        this.identifierIndexInCsvFile = identifierIndexInCsvFile;
        this.identifierFieldName = identifierFieldName;
        this.errorMessageCode = errorMessageCode;
        this.fieldsMapper = fieldsMapper;
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
                        String[] parts = new String(line.getBytes(), StandardCharsets.UTF_8).split(",");
                        if (parts[0].trim().equalsIgnoreCase(identifier)) {
                            for (Integer index : fieldsMapper.keySet()) {
                                if (index >= parts.length) {
                                    LOGGER.warn("can't find attribute[{}] value for[{}]", fieldsMapper.get(index), identifier);
                                } else {
                                    flowData.put(fieldsMapper.get(index), WebUtils.escapeString(parts[index]));
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

    public Map<Integer, String> getFieldsMapper() {
        return fieldsMapper;
    }

    public String getIdentifierFieldName() {
        return identifierFieldName;
    }

    public String getErrorMessageCode() {
        return errorMessageCode;
    }
}
