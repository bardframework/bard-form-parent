package org.bardframework.flow.processor.dataprovider;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bardframework.commons.web.utils.ResourceUtils;
import org.bardframework.commons.web.utils.WebUtils;
import org.bardframework.flow.exception.FlowExecutionException;
import org.bardframework.flow.processor.FormProcessorAbstract;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Getter
@Setter
@Slf4j
public class DataProviderCsvFileProcessor extends FormProcessorAbstract {

    protected final String csvFileLocation;
    protected final Map<Integer, String> mapper;
    protected final String identifierFieldName;
    protected final String identifierNotFoundErrorMessageCode;
    protected String separator = ",";

    public DataProviderCsvFileProcessor(String csvFileLocation, String identifierFieldName, Map<Integer, String> mapper, String identifierNotFoundErrorMessageCode) {
        this.csvFileLocation = csvFileLocation;
        this.identifierFieldName = identifierFieldName;
        this.identifierNotFoundErrorMessageCode = identifierNotFoundErrorMessageCode;
        this.mapper = mapper;
    }

    @Override
    public void process(String flowToken, Map<String, String> flowData, Map<String, String> formData, Locale locale, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
        String identifier = flowData.get(this.getIdentifierFieldName());
        if (StringUtils.isBlank(identifier)) {
            log.warn("identifier not exist for [{}], in csv file", flowData);
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
                                    log.warn("can't find attribute[{}] value for[{}]", mapper.get(index), identifier);
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
        log.warn("can't find record for[{}] in csv file [{}]", identifier, this.getCsvFileLocation());
        throw new FlowExecutionException(List.of(identifierNotFoundErrorMessageCode));
    }

}
