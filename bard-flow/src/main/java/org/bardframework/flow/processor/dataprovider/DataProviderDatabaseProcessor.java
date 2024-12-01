package org.bardframework.flow.processor.dataprovider;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.bardframework.commons.utils.StringTemplateUtils;
import org.bardframework.commons.utils.persian.PersianStringUtils;
import org.bardframework.flow.exception.FlowExecutionException;
import org.bardframework.flow.processor.FormProcessorAbstract;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Slf4j
@Getter
public class DataProviderDatabaseProcessor extends FormProcessorAbstract {

    private final DataSource dataSource;
    private final String fetchDataQuery;
    private final String errorMessageCode;

    public DataProviderDatabaseProcessor(DataSource dataSource, String fetchDataQuery, String errorMessageCode) {
        this.dataSource = dataSource;
        this.fetchDataQuery = fetchDataQuery;
        this.errorMessageCode = errorMessageCode;
    }

    @Override
    public void process(String flowToken, Map<String, Object> flowData, Map<String, Object> formData, Locale locale, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                try (ResultSet resultSet = statement.executeQuery(StringTemplateUtils.fillTemplate(fetchDataQuery, flowData))) {
                    if (!resultSet.next()) {
                        log.warn("data not found in db to fill form, [{}], [{}]", flowData, formData);
                        throw new FlowExecutionException(List.of(errorMessageCode));
                    }
                    for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                        flowData.put(resultSet.getMetaData().getColumnLabel(i), PersianStringUtils.disinfectPersianText(resultSet.getString(i)));
                    }
                }
            }
        }
    }

}
