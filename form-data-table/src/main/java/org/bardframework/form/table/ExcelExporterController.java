package org.bardframework.form.table;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.bardframework.commons.utils.ReflectionUtils;
import org.bardframework.crud.api.base.PagedData;
import org.bardframework.form.common.table.TableHeader;
import org.bardframework.form.common.table.TableModel;
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

public interface ExcelExporterController<M, C> {
    String APPLICATION_OOXML_SHEET = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy_MM_dd__HH_mm_ss");

    Map<String, PagedData<M>> getData(C criteria, Pageable page) throws Exception;

    TableModel getTableModel();

    String getMessage(String code);

    @PostMapping(path = "export", consumes = MediaType.APPLICATION_JSON_VALUE, produces = APPLICATION_OOXML_SHEET)
    default void export(@RequestBody @Validated C criteria, Locale locale, HttpServletResponse response) throws Exception {
        try (OutputStream outputStream = response.getOutputStream()) {
            response.setContentType(APPLICATION_OOXML_SHEET);
            response.addHeader("Content-Disposition", "attachment;filename=\"" + this.getOutputFileName(locale) + " \"");
            this.generateExcel(criteria, outputStream, locale);
        }
    }

    default void generateExcel(C criteria, OutputStream outputStream, Locale locale) throws Exception {
        this.generateExcel(this.getTableModel(), this.getData(criteria, Pageable.ofSize(Integer.MAX_VALUE)), outputStream, locale);
    }

    default void generateExcel(TableModel table, Map<String, PagedData<M>> data, OutputStream outputStream, Locale locale) throws Exception {
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

            PagedData<M> tableData = data.get(table.getName());
            this.createSheet(table, tableData.getList(), workbook, headerStyle, dataStyle, locale);

            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                workbook.getSheetAt(i).setRightToLeft(this.isRtl(locale));
            }
            workbook.write(outputStream);
        }
    }

    default void createSheet(TableModel model, List<M> list, Workbook workbook, CellStyle headerStyle, CellStyle dataStyle, Locale locale) throws Exception {
        Sheet sheet = workbook.createSheet(this.getSheetName(model));
        ((SXSSFSheet) sheet).trackAllColumnsForAutoSizing();
        int rowIndex = 0;
        Row headerRow = sheet.createRow(rowIndex++);
        /*
            write headers
        */
        int columnIndex = 0;
        for (TableHeader header : model.getHeaders()) {
            Cell cell = headerRow.createCell(columnIndex++);
            cell.setCellStyle(headerStyle);
            cell.setCellValue(this.getMessage(header.getTitle()));
        }

        /*
         ساخت استایل های مورد نیاز همه ی هدرها
         */
        Map<String, CellStyle> styles = new HashMap<>();
        for (TableHeader headerModel : model.getHeaders()) {
//            if (null != headerModel.getFormat()) {
//                CellStyle cellStyle = workbook.createCellStyle();
//                cellStyle.cloneStyleFrom(dataStyle);
//                cellStyle.setDataFormat((short) BuiltinFormats.getBuiltinFormat(headerModel.getFormat()));
//                styles.put(headerModel.getFormat(), cellStyle);
//            }
        }

        /*
            write data
        */
        Row row;
        for (M data : list) {
            columnIndex = 0;
            row = sheet.createRow(rowIndex++);
            for (TableHeader headerModel : model.getHeaders()) {
                Cell cell = row.createCell(columnIndex++);
                Object value = ReflectionUtils.getPropertyValue(data, headerModel.getName());
                cell.setBlank();
                /*if (null == value) {
                } else {
                    this.setValue(cell, value, headerModel.getDataType(), locale);
                }*/
                /*if (null != headerModel.getFormat()) {
                    cell.setCellStyle(styles.get(headerModel.getFormat()));
                } else {
                }*/
                cell.setCellStyle(dataStyle);
            }
        }
        for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
            //sheet.autoSizeColumn(i);
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

    default String getSheetName(TableModel table) {
        String key = null == table.getTitle() ? table.getName() : table.getTitle();
        return this.getMessage(key);
    }

    default void setValue(Cell cell, Object value, /*TableHeader.DataType dataType,*/ Locale locale) {
        if (null == value) {
            cell.setBlank();
            return;
        }
        if (Number.class.isAssignableFrom(value.getClass())) {
            cell.setCellValue(((Number) value).doubleValue());
            return;
        }
/*        if (this.isRtl(locale)) {
            if (dataType == TableHeader.DataType.DATE) {
                value = LocalDateJalali.of(value.toString()).format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            } else if (dataType == TableHeader.DataType.DATE_TIME) {
                value = LocalDateTimeJalali.of(value.toString()).format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
            }
        }*/
        cell.setCellValue(value.toString());
    }

    boolean isRtl(Locale locale);
}
