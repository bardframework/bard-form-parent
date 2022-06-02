package org.bardframework.form.table;

import org.bardframework.commons.utils.ReflectionUtils;
import org.bardframework.crud.api.base.BaseCriteria;
import org.bardframework.crud.api.base.BaseModel;
import org.bardframework.crud.api.base.BaseService;
import org.bardframework.crud.api.base.PagedData;
import org.bardframework.form.common.table.TableData;
import org.bardframework.form.common.table.TableHeader;
import org.bardframework.form.common.table.TableModel;
import org.bardframework.form.table.header.TableHeaderTemplate;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public interface TableModelRestController<M extends BaseModel<?>, C extends BaseCriteria<?>, S extends BaseService<M, C, ?, ?, U>, U> {

    @GetMapping(path = "table", produces = MediaType.APPLICATION_JSON_VALUE)
    default TableModel getTableModel(Locale locale) throws Exception {
        return TableUtils.toTable(this.getTableTemplate(), locale, Map.of());
    }

    @PostMapping(path = {"table/filter"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    default ResponseEntity<TableData> getFilteredData(@RequestBody @Validated C criteria, Pageable page) {
        PagedData<M> pagedData = this.getService().get(criteria, page, this.getUser());
        TableData tableData = this.toTableData(pagedData, this.getTableTemplate());
        return ResponseEntity.ok().body(tableData);
    }

    default TableData toTableData(PagedData<M> pagedData, TableTemplate tableTemplate) {
        TableData tableData = new TableData();
        tableData.setTotal(pagedData.getTotal());
        tableData.setHeaders(tableTemplate.getHeaderTemplates().stream().map(TableHeader::getName).collect(Collectors.toList()));
        for (M model : pagedData.getData()) {
            List<Object> values = new ArrayList<>();
            for (TableHeaderTemplate headerTemplate : tableTemplate.getHeaderTemplates()) {
                try {
                    Object value = ReflectionUtils.getPropertyValue(model, headerTemplate.getName());
                    values.add(headerTemplate.format(value));
                } catch (Exception e) {
                    throw new IllegalStateException(String.format("can't read property [%s] of [%s] instance and convert it, table [%s]", headerTemplate.getName(), model.getClass(), tableTemplate.getName()));
                }
            }
            tableData.addData(values);
        }
        return tableData;
    }

    TableTemplate getTableTemplate();

    S getService();

    U getUser();
}
