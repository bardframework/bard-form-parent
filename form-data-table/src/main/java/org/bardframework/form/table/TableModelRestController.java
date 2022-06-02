package org.bardframework.form.table;

import org.bardframework.form.common.table.TableModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Locale;

public interface TableModelRestController {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    TableModel getTableModel(Locale locale) throws Exception;
}
