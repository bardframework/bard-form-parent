package org.bardframework.form.table;

import org.bardframework.crud.api.base.BaseCriteria;
import org.bardframework.crud.api.base.BaseModel;
import org.bardframework.crud.api.base.BaseService;
import org.bardframework.crud.api.base.PagedData;
import org.bardframework.form.common.table.TableData;
import org.bardframework.form.common.table.TableModel;
import org.bardframework.form.table.utils.ExcelUtils;
import org.bardframework.form.table.utils.TableUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.Locale;
import java.util.Map;

public interface TableModelRestController<M extends BaseModel<?>, C extends BaseCriteria<?>, S extends BaseService<M, C, ?, ?, U>, U> {

    String APPLICATION_OOXML_SHEET = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    String TABLE_GET_URL = "table";
    String TABLE_FILTER_URL = "table/filter";
    String TABLE_EXPORT_URL = "table/export";

    TableTemplate getTableTemplate();

    S getService();

    U getUser();

    boolean isRtl(Locale locale);

    String getExportFileName(String contentType, Locale locale, U user);

    @GetMapping(path = TABLE_GET_URL, produces = MediaType.APPLICATION_JSON_VALUE)
    default TableModel getTableModel(Locale locale) throws Exception {
        return TableUtils.toTable(this.getTableTemplate(), locale, Map.of());
    }

    @PostMapping(path = TABLE_FILTER_URL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    default TableData getTableData(@RequestBody @Validated C criteria, Pageable page) {
        PagedData<M> pagedData = this.getService().get(criteria, page, this.getUser());
        return TableUtils.toTableData(pagedData, this.getTableTemplate());
    }

    @PostMapping(path = TABLE_EXPORT_URL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = APPLICATION_OOXML_SHEET)
    default void exportTable(@RequestBody @Validated C criteria, Locale locale, HttpServletResponse httpResponse) throws Exception {
        PagedData<M> pagedData = this.getService().get(criteria, Pageable.ofSize(Integer.MAX_VALUE), this.getUser());
        TableData tableData = TableUtils.toTableData(pagedData, this.getTableTemplate());
        try (OutputStream outputStream = httpResponse.getOutputStream()) {
            httpResponse.setContentType(APPLICATION_OOXML_SHEET);
            httpResponse.addHeader("Content-Disposition", "attachment;filename=\"" + this.getExportFileName(APPLICATION_OOXML_SHEET, locale, this.getUser()) + " \"");
            ExcelUtils.generateExcel(this.getTableTemplate(), tableData, outputStream, locale, this.isRtl(locale));
        }
    }
}
