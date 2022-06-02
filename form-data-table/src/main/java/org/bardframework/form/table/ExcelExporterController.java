package org.bardframework.form.table;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.bardframework.crud.api.base.BaseCriteria;
import org.bardframework.crud.api.base.BaseModel;
import org.bardframework.crud.api.base.BaseService;
import org.bardframework.crud.api.base.PagedData;
import org.bardframework.form.common.table.TableData;
import org.bardframework.form.common.table.TableHeader;
import org.bardframework.form.common.table.TableModel;
import org.bardframework.form.table.header.TableHeaderTemplate;
import org.bardframework.time.LocalDateTimeJalali;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public interface ExcelExporterController<M extends BaseModel<?>, C extends BaseCriteria<?>, S extends BaseService<M, C, ?, ?, U>, U> {
    String APPLICATION_OOXML_SHEET = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy_MM_dd__HH_mm_ss");

    @PostMapping(path = "table/export", consumes = MediaType.APPLICATION_JSON_VALUE, produces = APPLICATION_OOXML_SHEET)
    default void export(@RequestBody @Validated C criteria, Locale locale, HttpServletResponse response) throws Exception {
        PagedData<M> pagedData = this.getService().get(criteria, Pageable.ofSize(Integer.MAX_VALUE), this.getUser());
        TableData tableData = this.toTableData(pagedData, this.getTableTemplate());
        try (OutputStream outputStream = response.getOutputStream()) {
            response.setContentType(APPLICATION_OOXML_SHEET);
            response.addHeader("Content-Disposition", "attachment;filename=\"" + this.getOutputFileName(locale) + " \"");
            this.generateExcel(this.getTableTemplate(), tableData, outputStream, locale);
        }
    }

    TableTemplate getTableTemplate();

    S getService();

    U getUser();

    TableData toTableData(PagedData<M> pagedData, TableTemplate tableTemplate);

    default void generateExcel(TableTemplate tableTemplate, TableData tableData, OutputStream outputStream, Locale locale) throws Exception {
        try (Workbook workbook = new SXSSFWorkbook()) {
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 10);
            headerFont.setFontName("Tahoma");
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.LIGHT_BLUE.getIndex());
            headerStyle.setAlignment(HorizontalAlignment.GENERAL);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            Font dataFont = workbook.createFont();
            dataFont.setFontHeightInPoints((short) 10);
            dataFont.setFontName("Tahoma");
            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setFont(dataFont);
            dataStyle.setAlignment(HorizontalAlignment.GENERAL);
            dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            this.createSheet(workbook, tableTemplate, tableData, headerStyle, dataStyle, locale);

            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                workbook.getSheetAt(i).setRightToLeft(this.isRtl(locale));
            }
            workbook.write(outputStream);
        }
    }

    default void createSheet(Workbook workbook, TableTemplate tableTemplate, TableData tableData, CellStyle headerStyle, CellStyle dataStyle, Locale locale) throws Exception {
        TableModel tableModel = TableUtils.toTable(tableTemplate, locale, Map.of());
        Sheet sheet = workbook.createSheet(tableModel.getName());
        ((SXSSFSheet) sheet).trackAllColumnsForAutoSizing();
        int rowIndex = 0;
        Row headerRow = sheet.createRow(rowIndex++);
        /*
            write headers
        */
        int columnIndex = 0;
        for (TableHeader header : tableModel.getHeaders()) {
            Cell cell = headerRow.createCell(columnIndex++);
            cell.setCellStyle(headerStyle);
            cell.setCellValue(header.getTitle());
        }

        /*
         ساخت استایل های مورد نیاز همه ی هدرها
         */
        Map<String, CellStyle> styles = new HashMap<>();
        for (TableHeaderTemplate<?, ?> headerModel : tableTemplate.getHeaderTemplates()) {
            if (null != headerModel.getExcelFormat()) {
                CellStyle cellStyle = workbook.createCellStyle();
                cellStyle.cloneStyleFrom(dataStyle);
                cellStyle.setDataFormat((short) BuiltinFormats.getBuiltinFormat(headerModel.getExcelFormat()));
                styles.put(headerModel.getExcelFormat(), cellStyle);
            }
        }

        /*
            write data
        */
        for (List<Object> rowData : tableData.getData()) {
            Row row = sheet.createRow(rowIndex++);
            for (int index = 0; index < tableTemplate.getHeaderTemplates().size(); index++) {
                Cell cell = row.createCell(index++);
                Object cellValue = rowData.get(index);
                TableHeaderTemplate<?, ?> headerTemplate = tableTemplate.getHeaderTemplates().get(index);
                if (null == cellValue) {
                    cell.setBlank();
                } else {
                    if (Number.class.isAssignableFrom(cellValue.getClass())) {
                        cell.setCellValue(((Number) cellValue).doubleValue());
                    } else {
                        cell.setCellValue(cellValue.toString());
                    }
                }
                if (null != headerTemplate.getExcelFormat()) {
                    cell.setCellStyle(styles.get(headerTemplate.getExcelFormat()));
                } else {
                    cell.setCellStyle(dataStyle);
                }
            }
        }
        for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
            sheet.autoSizeColumn(i);
        }
        sheet.createFreezePane(0, 1);
    }

    default String getReportFileName() {
        return this.getClass().getSimpleName().replace("RestController", "");
    }

    default String getOutputFileName(Locale locale) {
        if (this.isRtl(locale)) {
            return this.getReportFileName() + "_" + LocalDateTimeJalali.now().format(DATE_TIME_FORMATTER) + ".xlsx";
        } else {
            return this.getReportFileName() + "_" + LocalDateTime.now().format(DATE_TIME_FORMATTER) + ".xlsx";
        }
    }

    boolean isRtl(Locale locale);
}
